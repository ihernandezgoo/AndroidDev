package com.example.hoopscoreb.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.hoopscoreb.ui.PossessionView
import kotlin.let
import kotlin.math.sqrt

class AccelerometerHandler(
    context: Context,
    private val possessionView: PossessionView
) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    fun register() {
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun unregister() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]
                val magnitude = sqrt(x * x + y * y + z * z)

                // Golpe fuerte → solo resetear posesión
                if (magnitude > 15f) {
                    possessionView.resetPossessionTime()
                } else {
                    // Inclinar móvil → cambiar flecha según eje X
                    possessionView.updateArrowByTilt(x)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}