package com.example.internalsentinel

import android.content.Context
import android.hardware.*
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Sensor Manager and Accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) ?: error("No Accelerometer found")

        setContent {
            SensorNavigationApp(sensorManager, accelerometer)
        }
    }
}

@Composable
fun SensorNavigationApp(
    sensorManager: SensorManager,
    accelerometer: Sensor
) {
    var isMeasuring by remember { mutableStateOf(false) }
    var distance by remember { mutableStateOf(0.0) }
    var pathLength by remember { mutableStateOf(0.0) }
    var accelerometerEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Distance and Path Length
        Text(text = "Distance from Origin: ${"%.2f".format(distance)} meters")
        Text(text = "Path Length: ${"%.2f".format(pathLength)} meters")

        Spacer(modifier = Modifier.height(16.dp))

        // Start and Stop Buttons
        Row {
            Button(
                onClick = {
                    isMeasuring = true
                    distance = 0.0 // Reset distance
                    pathLength = 0.0 // Reset path length
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) {
                Text("Start")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    isMeasuring = false
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text("Stop")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Accelerometer Checkbox
        Checkbox(
            checked = accelerometerEnabled,
            onCheckedChange = { enabled ->
                if (!isMeasuring) {
                    accelerometerEnabled = enabled
                }
            }
        )
        Text(text = "Enable Accelerometer")
    }
}
