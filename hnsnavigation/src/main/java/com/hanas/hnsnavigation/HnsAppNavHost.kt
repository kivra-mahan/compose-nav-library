package com.hanas.hnsnavigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun HnsNavigator.HnsAppNavHost(
    modifier: Modifier = Modifier,
    startDestination: HnsNavLocation<*>,
    builder: NavGraphBuilder.() -> Unit
) {
    val navController = rememberNavController()
    LaunchedEffect("navigation") {
        observe(navController, this)
    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.getRouteTemplate(),
        builder = builder
    )
}