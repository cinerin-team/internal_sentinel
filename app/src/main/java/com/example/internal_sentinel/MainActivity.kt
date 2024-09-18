package com.example.internalsentinel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize sensor manager and sensors
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
            ?: error("No Accelerometer found on this device.")

        setContent {
            SensorNavigationApp(sensorManager, accelerometer)
        }
    }
}

@Composable
fun SensorNavigationApp(sensorManager: SensorManager, accelerometer: Sensor) {
    var isMeasuring by remember { mutableStateOf(false) }
    var distance by remember { mutableStateOf(0.0) }
    var pathLength by remember { mutableStateOf(0.0) }
    var accelerometerEnabled by remember { mutableStateOf(true) }

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                try {
                    if (event?.sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION && isMeasuring) {
                        val deltaDistance = processAccelerometerData(event)
                        if (!deltaDistance.isNaN() && deltaDistance >= 0) {
                            distance += deltaDistance
                            pathLength += abs(deltaDistance) // Simplified path length
                        }
                    }
                } catch (e: Exception) {
                    // Handle exceptions
                    e.printStackTrace()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not implemented
            }
        }
    }

    LaunchedEffect(isMeasuring) {
        if (isMeasuring) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Distance from Origin: ${"%.2f".format(distance)} meters")
        Text(text = "Path Length: ${"%.2f".format(pathLength)} meters")

        Spacer(modifier = Modifier.height(16.dp))

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

fun processAccelerometerData(event: SensorEvent): Double {
    val acceleration = event.values[0] // Assuming linear acceleration in X-axis
    val timeInterval = 0.02 // Example time interval (adjust based on actual sensor delay)
    return acceleration * timeInterval // Simplified distance calculation
}
