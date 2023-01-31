package com.hanas.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.hanas.hnsnavigation.HnsAppNavHost
import com.hanas.hnsnavigation.HnsNavLocationScope
import com.hanas.hnsnavigation.composable
import com.hanas.hnsnavigation.hnsNavigator
import com.hanas.myapplication.navigation.Goodbye
import com.hanas.myapplication.navigation.Greeting
import com.hanas.myapplication.ui.Goodbye
import com.hanas.myapplication.ui.Greeting
import com.hanas.myapplication.ui.GreetingNavEvent
import com.hanas.myapplication.ui.theme.MyApplicationTheme
import com.hanas.myapplication.viewmodel.GoodbyeViewModel
import com.hanas.myapplication.viewmodel.GreetingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                hnsNavigator.HnsAppNavHost(
                    startDestination = Greeting
                ) {
                    composable(Greeting) { lazyNavArgs ->
                        val navArgs by lazyNavArgs
                        val viewModel = hiltViewModel<GreetingViewModel>()
                        Greeting(
                            firstName = navArgs?.nickname.orEmpty(),
                            surname = viewModel.title,
                            onNavEvent = { navigateFromGreeting(it) }
                        )
                    }
                    composable(Goodbye) {
                        val viewModel = hiltViewModel<GoodbyeViewModel>()
                        Goodbye(viewModel.nickname) {
                            hnsNavigator.scopedNavigateTo(Greeting, Greeting.Data(viewModel.nickname))
                        }
                    }
                }
            }
        }
    }

    private fun HnsNavLocationScope<Greeting>.navigateFromGreeting(event: GreetingNavEvent) {
        with(hnsNavigator) {
            when (event) {
                GreetingNavEvent.CLICK -> scopedNavigateTo(Goodbye)
                GreetingNavEvent.BACK -> back()
            }
        }
    }
}