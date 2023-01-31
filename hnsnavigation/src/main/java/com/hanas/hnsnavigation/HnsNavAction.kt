package com.hanas.hnsnavigation

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

sealed class HnsNavAction {
    class Navigate<T : Parcelable>(
        val destination: HnsNavLocation<T>,
        val navArgs: T,
        val navOptionsBuilder: NavOptionsBuilder.(navController: NavController) -> Unit = {}
    ) : HnsNavAction()
    object Back : HnsNavAction()
}