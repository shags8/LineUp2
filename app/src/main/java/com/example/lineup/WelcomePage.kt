package com.example.lineup

import android.Manifest
import android.Manifest.permission
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class WelcomePage : AppCompatActivity() {

    private lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>
    private var isLocationPermissionGranted=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions->
            isLocationPermissionGranted= permissions[permission.ACCESS_FINE_LOCATION]?:isLocationPermissionGranted

       }

        requestPermission()
    }


    private fun requestPermission(){
        isLocationPermissionGranted=ContextCompat.checkSelfPermission(
            this,
            permission.ACCESS_FINE_LOCATION
        )==PackageManager.PERMISSION_GRANTED



        val permissionRequest= mutableListOf<String>()

        if(!isLocationPermissionGranted){
            permissionRequest.add(permission.ACCESS_FINE_LOCATION)
        }

        if(permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }
}