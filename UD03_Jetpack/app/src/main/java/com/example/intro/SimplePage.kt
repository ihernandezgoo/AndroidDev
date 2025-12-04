package com.example.intro

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header() {
    Text(
        text = "Page Header",
        fontSize = 24.sp,
    )
}

@Composable
fun SimpleText() {
    Text(
        text = "This is a simple page",
        fontSize = 16.sp,
    )
}

@Composable
fun ActionButton() {
    val context = LocalContext.current
    Button(onClick = {
        val text = "Hello toast!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }) {
        Text(text = "Click me")
    }
}


@Preview(showBackground = true)
@Composable
fun SimplePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header()
        Spacer(Modifier.height(8.dp))
        SimpleText()
        Spacer(Modifier.height(8.dp))
        ActionButton()
    }
}
