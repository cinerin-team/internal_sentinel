package com.example.internalsentinel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternalSentinelApp()
        }
    }
}

@Composable
fun InternalSentinelApp() {
    var distance by remember { mutableStateOf(0.0) }
    var pathLength by remember { mutableStateOf(0.0) }
    var isMeasuring by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                distance = 0.0
                pathLength = 0.0
                isMeasuring = true
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGray),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Start")
        }

        Button(
            onClick = { isMeasuring = false },
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGray),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Stop")
        }

        Text("Distance: $distance meters")
        Text("Path Length: $pathLength meters")
    }
}
