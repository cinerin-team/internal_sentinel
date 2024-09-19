package com.example.internal_sentinel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var sensorEventListener: SensorEventListener? = null

    private var distanceTextView: TextView? = null
    private var pathTextView: TextView? = null
    private var startButton: Button? = null
    private var stopButton: Button? = null
    private var accelerometerCheckbox: CheckBox? = null

    private var isMeasuring: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        distanceTextView = findViewById(R.id.distanceTextView)
        pathTextView = findViewById(R.id.pathTextView)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        accelerometerCheckbox = findViewById(R.id.accelerometer_checkbox)

        // Initialize SensorManager and Sensors
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        // Initialize buttons and checkbox logic
        startButton?.setOnClickListener { startMeasurement() }
        stopButton?.setOnClickListener { stopMeasurement() }

        stopButton?.isEnabled = false
    }

    private fun startMeasurement() {
        if (!isMeasuring) {
            // Clear previous data
            distanceTextView?.text = "Distance: 0"
            pathTextView?.text = "Path Length: 0"

            // Check if accelerometer checkbox is checked
            val useAccelerometer = accelerometerCheckbox?.isChecked ?: false

            if (useAccelerometer) {
                // Register accelerometer listener
                sensorEventListener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent) {
                        // Your logic for handling accelerometer sensor data
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                        // Handle accuracy changes if needed
                    }
                }
                sensorManager?.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            }

            // Disable checkbox and start button during measurement
            accelerometerCheckbox?.isEnabled = false
            startButton?.isEnabled = false
            stopButton?.isEnabled = true
            isMeasuring = true
        }
    }

    private fun stopMeasurement() {
        if (isMeasuring) {
            sensorManager?.unregisterListener(sensorEventListener)
            // Re-enable checkbox and start button after stopping measurement
            accelerometerCheckbox?.isEnabled = true
            startButton?.isEnabled = true
            stopButton?.isEnabled = false
            isMeasuring = false
        }
    }
}
