package com.hanas.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hanas.myapplication.navigation.Greeting
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random
var greetingViewModelCounter = 0

@HiltViewModel
class GreetingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context
) : ViewModel() {
    private val navArgs by Greeting.navArgs(savedStateHandle)
    val title = greetingViewModelCounter.toString()
    init {
        greetingViewModelCounter += 1
    }
}

