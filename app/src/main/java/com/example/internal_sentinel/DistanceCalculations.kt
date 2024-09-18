package com.example.internalsentinel

import android.hardware.SensorEvent

fun processAccelerometerData(event: SensorEvent): Double {
    // Linear acceleration data processing logic.
    // Return the calculated distance from the sensor event values.
    return event.values[0].toDouble() // Simplified for testing
}
