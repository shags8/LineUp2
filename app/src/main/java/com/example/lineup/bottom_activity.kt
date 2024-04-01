package com.example.lineup

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.lineup.databinding.ActivityBottomBinding
import com.google.firebase.messaging.FirebaseMessaging
import gen._base._base_java__assetres.srcjar.R.id.text

class bottom_activity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var binding: ActivityBottomBinding


    private val directionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == DirectionService.ACTION_DIRECTION_UPDATE) {
                    val direction = it.getStringExtra("DIRECTION")
                    if (direction != null) {
                        updateDirectionUI(direction)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        val serviceIntent = Intent(this, DirectionService::class.java)
        startService(serviceIntent)

        binding = ActivityBottomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"

        Log.e("id16", "$retrievedValue")
        replaceFragments(Qr_code())


        val bottomNavBar = binding.bottomNavigationView
        bottomNavBar.itemIconTintList = null
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.Leaderboard -> replaceFragments(Leaderboard())
                R.id.QR_code -> replaceFragments(Qr_code())
                R.id.route -> replaceFragments(route())
                R.id.Scanner -> replaceFragments(scanner())
            }
            true
        }
        if (checkCameraPermission()) {
            //
        } else {
            requestCameraPermission()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start location-related task or request notifications
                    // e.g., startLocationUpdates()
                    // e.g., createNotificationChannel()
                } else {
                    // Permission denied
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show explanation dialog and request permission again
                        requestCameraPermission()
                    } else {
                        // Permission permanently denied
                        // Show settings to enable permission manually
                        showSettingsDialog()
                    }
                }
            }
        }
    }

    private fun showSettingsDialog() {
        Toast.makeText(this, "Camera permission required!", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent) // Start the activity to open settings
    }


    private fun updateDirectionUI(direction: String) {
        // Update your UI elements (e.g., TextView) with the direction information
        binding.direction.text = direction
    }

    private fun replaceFragments(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { dialog, which ->
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, which ->
                // Handle "no" button click or remove this block if not needed
            }
            .show()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(directionReceiver)
        // Start the LocationUpdates service when activity goes into background
        startbackground()
        stopforeground()
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this, DirectionService::class.java)
        stopService(serviceIntent)
        stopbackground()
        stopforeground()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        // Stop the LocationUpdates service when activity comes back to foreground
        Log.e("abc1", "1")
        startforeground()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                directionReceiver,
                IntentFilter(DirectionService.ACTION_DIRECTION_UPDATE)
            )
        Log.e("abc1", "2")
        stopbackground()
        Log.e("abc1", "3")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startforeground() {
        val serviceIntent2 = Intent(this, ForeGroundLocationUpdates::class.java)
        startService(serviceIntent2)
        val serviceIntent3 = Intent(this, DirectionService::class.java)
        startService(serviceIntent3)
    }

    private fun stopforeground() {
        val serviceIntent2 = Intent(this, ForeGroundLocationUpdates::class.java)
        stopService(serviceIntent2)
    }

    private fun startbackground() {
        val serviceIntent = Intent(this, LocationUpdates::class.java)
        startService(serviceIntent)
    }

    private fun stopbackground() {
        val serviceIntent = Intent(this, LocationUpdates::class.java)
        stopService(serviceIntent)
    }


    fun clearSharedPreferences(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun signOutAndNavigateToMain(context: Context) {
        clearSharedPreferences(context)

        val intent = Intent(context, WelcomePage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

        if (context is Activity) {
            context.finish()
        }
    }
}
