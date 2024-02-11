package com.example.lineup // Replace with your package name

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class LocationUpdates : Service() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var socket: Socket
    private lateinit var locationManager: LocationManager
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            sendLocationToBackend(location)
        }
    }

    override fun onCreate() {
        super.onCreate()


        // Replace with your backend server URL
        Log.e("id16" , "stop2121")
        val serverUrl = "https://lineup-backend.onrender.com"

        try {
            socket = IO.socket(serverUrl)
            socket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check for location permissions
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("LocationUpdateService", "Location permission not granted")
            // Handle permission request if needed
        } else {
            requestLocationUpdates()
        }

        // Create notification for foreground service
        val notification = createNotification(this)
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
        locationManager.removeUpdates(locationListener)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null // Not a bound service
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, locationListener)
    }

    private fun sendLocationToBackend(location: Location) {
        val data = JSONObject()
        sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
        try {
            data.put("latitude", location.latitude)
            data.put("longitude", location.longitude)
            data.put("token", retrievedValue)
            socket.emit("locationChange", data)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun createNotification(context: Context): Notification {
        val channelId = "location_update_channel"
        val notificationManager = getSystemService(NotificationManager::class.java)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Location Updates",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Updates Running")
            .setContentText("Sending location data to server...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
        // Replace with your icon

        return notificationBuilder.build()
    }
}

class ForeGroundLocationUpdates : Service() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var socket: Socket
    private lateinit var locationManager: LocationManager
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            sendLocationToBackend(location)
        }
    }

    override fun onCreate() {
        super.onCreate()


        // Replace with your backend server URL
        Log.e("id1236", "service3")
        Log.e("id16" , "stop2121")
        val serverUrl = "https://lineup-backend.onrender.com"

        try {
            socket = IO.socket(serverUrl)
            socket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check for location permissions
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("LocationUpdateService", "Location permission not granted")
            // Handle permission request if needed
        } else {
            requestLocationUpdates()
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
        locationManager.removeUpdates(locationListener)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null // Not a bound service
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, locationListener)
    }

    private fun sendLocationToBackend(location: Location) {
        val data = JSONObject()
        sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
        try {
            data.put("latitude", location.latitude)
            data.put("longitude", location.longitude)
            data.put("token", retrievedValue)
            socket.emit("locationChange", data)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}


