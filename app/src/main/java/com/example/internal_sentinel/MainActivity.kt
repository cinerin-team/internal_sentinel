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

class MainActivity : ComponentActivity() {
    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the sensor manager and accelerometer sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
            ?: error("No Accelerometer found")

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
                if (event?.sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION && isMeasuring) {
                    // Calculate distance using the accelerometer
                    distance += processAccelerometerData(event)
                    pathLength += processPathLength(event)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not implemented
            }
        }
    }

    LaunchedEffect(isMeasuring) {
        if (isMeasuring) {
            // Register sensor listener when measuring starts
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            // Unregister sensor listener when measuring stops
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
    // Simplified logic to calculate distance from accelerometer data
    val acceleration = event.values[0] // Linear acceleration in X-axis
    val timeInterval = 0.02 // Example time interval (replace with actual time difference between events)
    return acceleration * timeInterval // Distance = acceleration * time_interval (simplified)
}

fun processPathLength(event: SensorEvent): Double {
    // This can use a similar logic as processAccelerometerData
    return event.values[0] * 0.02 // Example path length calculation
}
