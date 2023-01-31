package com.hanas.myapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hanas.myapplication.ui.theme.MyApplicationTheme

@Composable
fun Goodbye(name: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = "Goodbye $name",
            modifier = modifier.clickable { onClick() },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoodbyePreview() {
    MyApplicationTheme {
        Goodbye("Android") {}
    }
}