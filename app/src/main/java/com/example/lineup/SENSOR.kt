import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorManagerHelper(private val context: Context) {

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private var sensorEventListener: SensorEventListener? = null

    fun startSensorUpdates(callback: (String) -> Unit) {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        if (sensor != null) {
            sensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    val azimuth = event?.values?.get(0) ?: 0f
                    val direction = getCardinalDirection(azimuth)
                    callback(direction)
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Handle accuracy changes if needed
                }
            }
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME)
        } else {
            // Handle unavailable sensor (display a message)
        }
    }

    fun stopSensorUpdates() {
        sensorEventListener?.let { sensorManager.unregisterListener(it) }
        sensorEventListener = null
    }

    private fun getCardinalDirection(azimuth: Float): String {
        when {
            azimuth >= 315 || azimuth < 45 -> return "North"
            azimuth >= 45 && azimuth < 135 -> return "East"
            azimuth >= 135 && azimuth < 225 -> return "South"
            else -> return "West"
        }
    }
}
