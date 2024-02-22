package com.example.lineup


import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class DirectionService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private lateinit var directionReceiver: BroadcastReceiver

    override fun onCreate() {
        super.onCreate()
        Log.e("id12367", "service2")
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // Update accelerometer data
            accelerometerData = event.values.clone()
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            // Update magnetometer data
            magnetometerData = event.values.clone()
        }

        updateOrientation()
    }

    private var accelerometerData = FloatArray(3)
    private var magnetometerData = FloatArray(3)

    private fun updateOrientation() {
        val rotationMatrix = FloatArray(9)
       // Log.e("id12367", "service23")
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerData, magnetometerData)) {
            val orientationValues = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationValues)

            // Convert radians to degrees
            val azimuthInDegrees = Math.toDegrees(orientationValues[0].toDouble()).toFloat()

            // Ensure azimuth is between 0 and 360 degrees
            val azimuth = if (azimuthInDegrees < 0) azimuthInDegrees + 360 else azimuthInDegrees

         //   Log.e("id12367", "service25")
            val direction = when {
                (azimuth >= 0 && azimuth < 22.5) || (azimuth in 337.5..360.0) -> "North"
                azimuth >= 22.5 && azimuth < 67.5 -> "Northeast"
                azimuth >= 67.5 && azimuth < 112.5 -> "East"
                azimuth >= 112.5 && azimuth < 157.5 -> "Southeast"
                azimuth >= 157.5 && azimuth < 202.5 -> "South"
                azimuth >= 202.5 && azimuth < 247.5 -> "Southwest"
                azimuth >= 247.5 && azimuth < 292.5 -> "West"
                azimuth >= 292.5 && azimuth < 337.5 -> "Northwest"
                else -> "Unknown"
            }
            Log.e("id12367", "$direction")
            val intent = Intent(ACTION_DIRECTION_UPDATE)
            intent.putExtra("DIRECTION", direction)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

        }
    }
    companion object {
        const val ACTION_DIRECTION_UPDATE = "com.example.lineup.DIRECTION_UPDATE"
    }
}
