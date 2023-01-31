package com.hanas.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random
var viewModelCounter = 0

@HiltViewModel
class GoodbyeViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    val nickname = viewModelCounter.toString()
    init {
        viewModelCounter += 1
    }
}