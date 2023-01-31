package com.hanas.hnsnavigation

import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.google.gson.Gson
import kotlin.reflect.KClass

abstract class HnsNavLocation<T : Parcelable>(
    private val name: String,
    private val dataClass: KClass<T>,
    private val configurations: Lazy<Map<HnsNavLocation<*>, HnsNavOptionsBuilder>>
) {
    fun getRouteTemplate() = "$name/{data}"

    fun getNavRoute(data: Parcelable) = "$name/${Uri.encode(Gson().toJson(data))}"

    fun getNavType() = HnsNavType(dataClass)

    fun <D : HnsNavLocation<*>> getConfiguration(destination: D) =
        configurations.value.getOrElse(destination) { HnsNavOptionsBuilder() }

    fun navArgs(savedStateHandle: SavedStateHandle): Lazy<T?> = lazy {
        Log.d("HANAS", "Lazy get from savedState")
        savedStateHandle["data"]
    }

    fun navArgs(navBackStackEntry: NavBackStackEntry): Lazy<T?> = lazy {
//        Log.d("HANAS", "Lazy get from navBackStack")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            navBackStackEntry.arguments?.getParcelable("data", dataClass.java)
        } else {
            @Suppress("DEPRECATION") navBackStackEntry.arguments?.getParcelable("data")
        }
    }
}

data class HnsNavOptionsBuilder(
    val builder: NavOptionsBuilder.(navController: NavController) -> Unit = {}
) {
    constructor(
        destination: HnsNavLocation<*>,
        behaviour: Behaviour,
        sameOnTopBehaviour: SameOnTopBehaviour
    ) : this(
        builder = { hnsNavOptionsBuilder(destination, it, behaviour, sameOnTopBehaviour) }
    )
}

fun HnsNavConfigurationPair(
    destination: HnsNavLocation<*>,
    behaviour: Behaviour,
    sameOnTopBehaviour: SameOnTopBehaviour
) = destination to HnsNavOptionsBuilder(destination, behaviour, sameOnTopBehaviour)

enum class SameOnTopBehaviour {
    ADD_NEW_ON_TOP,
    REPLACE,
    OPEN_EXISTING
}

enum class Behaviour {
    BACK_TO_LAST_INSTANCE_OF_DESTINATION,
    POP_CURRENT,
    ADD_ON_TOP
}

fun NavOptionsBuilder.hnsNavOptionsBuilder(
    destination: HnsNavLocation<*>,
    navController: NavController,
    behaviour: Behaviour,
    sameOnTopBehaviour: SameOnTopBehaviour
) {
    val backstack = navController.backQueue
    when (behaviour) {
        Behaviour.BACK_TO_LAST_INSTANCE_OF_DESTINATION -> {
            val routeToPop = destination.getRouteTemplate()
            popForParams(routeToPop, sameOnTopBehaviour, destination)
        }

        Behaviour.POP_CURRENT -> {
            val routeToPop = backstack.getOrNull(backstack.size - 2)?.destination?.route
            popForParams(routeToPop, sameOnTopBehaviour, destination)
        }

        Behaviour.ADD_ON_TOP -> {
            val routeToPop = backstack.lastOrNull()?.destination?.route
            popForParams(routeToPop, sameOnTopBehaviour, destination)
        }
    }

}

private fun NavOptionsBuilder.popForParams(
    routeToPop: String?,
    sameOnTopBehaviour: SameOnTopBehaviour,
    destination: HnsNavLocation<*>
) {
    popUpTo(routeToPop ?: return) {
        inclusive = shouldPopInclusive(sameOnTopBehaviour, routeToPop, destination)
    }
    launchSingleTop =
        sameOnTopBehaviour == SameOnTopBehaviour.OPEN_EXISTING
}

private fun shouldPopInclusive(
    sameOnTopBehaviour: SameOnTopBehaviour,
    routeToPop: String?,
    navDestination: HnsNavLocation<*>
) =
    sameOnTopBehaviour == SameOnTopBehaviour.REPLACE && routeToPop == navDestination.getRouteTemplate()