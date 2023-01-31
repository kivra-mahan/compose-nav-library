package com.hanas.hnsnavigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun <T : Parcelable, D : HnsNavLocation<T>> NavGraphBuilder.composable(
    navTarget: D,
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable HnsNavLocationScope<D>.(navArgs: Lazy<T?>) -> Unit
) {
    val navType = navTarget.getNavType()
    val route = navTarget.getRouteTemplate()
    val arguments = listOf(navArgument("data") { type = navType })
    composable(route, arguments, deepLinks) { navBackStackEntry ->
        val navArgs = navTarget.navArgs(navBackStackEntry)
        HnsNavLocationScopeImpl(navTarget).content(navArgs)
    }
}

inline fun <T : Parcelable, reified V : ViewModel> NavGraphBuilder.composableWithViewModel(
    navTarget: HnsNavLocation<T>,
    deepLinks: List<NavDeepLink> = emptyList(),
    crossinline content: @Composable (T?, V) -> Unit
) {
    val navType = navTarget.getNavType()
    val route = navTarget.getRouteTemplate()
    val arguments = listOf(navArgument("data") { type = navType })
    composable(route, arguments, deepLinks) { navBackStackEntry ->
        val navArgs by navTarget.navArgs(navBackStackEntry)
        val viewModel = hiltViewModel<V>()
        content(navArgs, viewModel)
    }
}


fun <K, V> lazyMapOf(vararg pairs: Pair<K, V>): Lazy<Map<K, V>> = lazy {
    mapOf(*pairs)
}