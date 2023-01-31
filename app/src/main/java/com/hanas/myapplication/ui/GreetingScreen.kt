package com.hanas.myapplication.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hanas.myapplication.ui.theme.MyApplicationTheme
enum class GreetingNavEvent {
    CLICK, BACK
}

@Composable
fun Greeting(
    firstName: String,
    surname: String,
    modifier: Modifier = Modifier,
    onNavEvent: (GreetingNavEvent) -> Unit,
) {
    BackHandler {
        onNavEvent(GreetingNavEvent.BACK)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = "Hello $firstName $surname",
            modifier = modifier.clickable { onNavEvent(GreetingNavEvent.CLICK) },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android", "123") {}
    }
}