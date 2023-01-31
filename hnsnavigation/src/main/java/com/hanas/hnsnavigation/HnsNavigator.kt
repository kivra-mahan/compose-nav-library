package com.hanas.hnsnavigation

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

val hnsNavigator: HnsNavigator = HnsNavigatorImpl()

interface HnsNavigator {
    fun <A : Parcelable> navigateFromTo(
        current: HnsNavLocation<*>,
        destination: HnsNavLocation<A>,
        navArgs: A
    )

    fun navigateFromTo(
        current: HnsNavLocation<*>,
        destination: HnsNavLocation<HnsNavNoData>
    )

    fun <A : Parcelable> navigateTo(
        destination: HnsNavLocation<A>,
        navArgs: A,
        navOptionsBuilder: NavOptionsBuilder.(navController: NavController) -> Unit = {}
    )

    fun navigateTo(
        destination: HnsNavLocation<HnsNavNoData>,
        navOptionsBuilder: NavOptionsBuilder.(navController: NavController) -> Unit = {}
    )

    fun back()

    fun observe(
        navController: NavHostController,
        scope: CoroutineScope
    )
}


class HnsNavigatorImpl : HnsNavigator {

    private val sharedFlow =
        MutableSharedFlow<HnsNavAction>(extraBufferCapacity = 1)

    override fun <A : Parcelable> navigateFromTo(
        current: HnsNavLocation<*>,
        destination: HnsNavLocation<A>,
        navArgs: A
    ) {
        val navOptionsBuilder = current.getConfiguration(destination).builder
        val action = HnsNavAction.Navigate(destination, navArgs, navOptionsBuilder)
        sharedFlow.tryEmit(action)
    }

    override fun navigateFromTo(
        current: HnsNavLocation<*>,
        destination: HnsNavLocation<HnsNavNoData>
    ) {
        val navOptionsBuilder = current.getConfiguration(destination).builder
        val action = HnsNavAction.Navigate(destination, HnsNavNoData, navOptionsBuilder)
        sharedFlow.tryEmit(action)
    }

    override fun <A : Parcelable> navigateTo(
        destination: HnsNavLocation<A>,
        navArgs: A,
        navOptionsBuilder: NavOptionsBuilder.(navController: NavController) -> Unit
    ) {
        val action = HnsNavAction.Navigate(destination, navArgs, navOptionsBuilder)
        sharedFlow.tryEmit(action)
    }

    override fun navigateTo(
        destination: HnsNavLocation<HnsNavNoData>,
        navOptionsBuilder: NavOptionsBuilder.(navController: NavController) -> Unit
    ) {
        val action = HnsNavAction.Navigate(destination, HnsNavNoData, navOptionsBuilder)
        sharedFlow.tryEmit(action)
    }

//    fun <T : Parcelable> navigateTo(navTarget: HnsNavLocation<T>, navArgs: T) {
//        val action = HnsNavAction.Navigate(navTarget, navArgs)
//        sharedFlow.tryEmit(action)
//    }
//
//    internal fun <T : Parcelable, D : HnsNavLocation<T>> navigate(
//        currentLocation: HnsNavLocation<*>,
//        destination: D,
//        navArgs: T
//    ) {
//        with(currentLocation.getConfiguration(destination)) {
//            val action = HnsNavAction.Navigate(destination, navArgs, builder)
//            sharedFlow.tryEmit(action)
//        }
//    }
//
//    internal fun navigate(
//        currentLocation: HnsNavLocation<*>,
//        destination: HnsNavLocation<HnsNavNoData>
//    ) {
//        with(currentLocation.getConfiguration(destination)) {
//            val action =
//                HnsNavAction.Navigate(destination, HnsNavNoData, builder)
//            sharedFlow.tryEmit(action)
//        }
//    }

//    internal fun navigateTo(navTarget: HnsNavLocation<HnsNavNoData>) {
//        val action = HnsNavAction.Navigate(navTarget, HnsNavNoData) {}
//        sharedFlow.tryEmit(action)
//    }

    override fun back() {
        sharedFlow.tryEmit(HnsNavAction.Back)
    }

    override fun observe(navController: NavHostController, scope: CoroutineScope) {
        sharedFlow.onEach { action ->
            when (action) {
                HnsNavAction.Back -> navController.popBackStack()
                is HnsNavAction.Navigate<*> -> {
                    navController.navigate(
                        route = action.destination.getNavRoute(action.navArgs),
                        builder = { action.navOptionsBuilder(this, navController) }
                    )
                }
            }
        }.launchIn(scope)
    }

}