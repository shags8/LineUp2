package com.example.lineup
import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lineup.databinding.ActivityBottomBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.yourpackage.ForeGroundLocationUpdates
import com.yourpackage.LocationUpdates
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.concurrent.atomic.AtomicBoolean


class bottom_activity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var socket : Socket
    private val locationServiceRunning = AtomicBoolean(false)



    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isCameraPermissionGranted = false
    private var isLocationPermissionGranted = false
    val permissionRequest = mutableListOf<String>()

    private lateinit var binding: ActivityBottomBinding
    override fun onCreate(savedInstanceState: Bundle?) {


        binding = ActivityBottomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"

        Log.e("id16","$retrievedValue")
        replaceFragments(Qr_code())

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isCameraPermissionGranted =
                    permissions[Manifest.permission.CAMERA] ?: isCameraPermissionGranted
                if (!isCameraPermissionGranted) {
                    showPermissionDeniedDialog()
                }

            }
        requestPermission()

        val bottomNavBar = binding.bottomNavigationView
        bottomNavBar.itemIconTintList = null
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.Leaderboard -> replaceFragments(Leaderboard())
                R.id.QR_code -> replaceFragments(Qr_code())
                R.id.route -> replaceFragments(route())
                R.id.Scanner -> replaceFragments(scanner())
            }
            return@setOnItemSelectedListener true


        }
    }


    private fun showPermissionDeniedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission Required")
        builder.setMessage("LineUp requires Camera access to function properly.")
        builder.setPositiveButton("Grant Permission") { dialog, which ->
            permissionRequest.add(Manifest.permission.CAMERA)
            permissionLauncher.launch(permissionRequest.toTypedArray())
            dialog.dismiss()
        }
        builder.setNegativeButton("Exit") { dialog, which ->
            dialog.dismiss()
            finish()
        }
        val dialog = builder.create()
        dialog.show()

    }

    private fun requestPermission() {
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!isCameraPermissionGranted) {
            permissionRequest.add(Manifest.permission.CAMERA)
        }

        isLocationPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!isLocationPermissionGranted) {
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }

    private fun replaceFragments(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure?")
            .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            })
            .setNegativeButton("no", DialogInterface.OnClickListener { dialog, which ->
                //   super.onBackPressed()
            })
            .show()
        // super.onBackPressed()
    }

    private fun connectSocketIO() {
        val serverUrl = "https://lineup-backend.onrender.com/"

        try {
            socket = IO.socket(serverUrl)
            socket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

//    private lateinit var locationManager: LocationManager
//    private val locationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            sendLocationToBackend(location)
//        }
//        // ... other callback methods
//    }

//    private fun startLocationUpdates() {
//        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0f, locationListener)
//    }
//    private fun sendLocationToBackend(location: Location) {
//        sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
//        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
//        val data = JSONObject()
//        try {
//            data.put("latitude", location.latitude)
//            data.put("longitude", location.longitude)
//            data.put("token", retrievedValue)
//            socket.emit("locationChange", data)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//    }
    override fun onPause() {
        super.onPause()
        // Start the LocationUpdates service when activity goes into background
        startbackground()
    stopforeground()
    }

    override fun onDestroy() {
        super.onDestroy()

       stopbackground()
        stopforeground()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        // Stop the LocationUpdates service when activity comes back to foreground
        Log.e("abc1","1")
        startforeground()
        Log.e("abc1","2")
        stopbackground()
        Log.e("abc1","3")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startforeground()
    {
        val serviceIntent2 = Intent(this, ForeGroundLocationUpdates::class.java)
        startForegroundService(serviceIntent2)
    }
    private fun stopforeground()
    {
        val serviceIntent2 = Intent(this, ForeGroundLocationUpdates::class.java)
        stopService(serviceIntent2)
    }
    private fun startbackground()
    {
        val serviceIntent = Intent(this, LocationUpdates::class.java)
        startService(serviceIntent)
    }
    private fun stopbackground()
    {
        val serviceIntent = Intent(this, LocationUpdates::class.java)
        stopService(serviceIntent)
    }

}
