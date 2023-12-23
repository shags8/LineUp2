package com.example.lineup

import android.Manifest
import android.Manifest.permission
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class WelcomePage : AppCompatActivity() {

    private lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>
    private var isLocationPermissionGranted=false
    val permissionRequest= mutableListOf<String>()

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)


        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permissions->
            isLocationPermissionGranted= permissions[permission.ACCESS_FINE_LOCATION]?:isLocationPermissionGranted
            if (!isLocationPermissionGranted ) {
                showPermissionDeniedDialog()
            }

        }
        requestPermission()


        auth=FirebaseAuth.getInstance()
        val user=auth.currentUser
        if(user!=null){
            startActivity(Intent(this,bottom_activity::class.java))
            finish()
        }

    }



    private fun showPermissionDeniedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission Required")
        builder.setMessage("LineUp requires location permission to function properly.")
        builder.setPositiveButton("Grant Permission") { dialog, which ->
            permissionRequest.add(permission.ACCESS_FINE_LOCATION)
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


    private fun requestPermission(){
        isLocationPermissionGranted=ContextCompat.checkSelfPermission(
            this,
            permission.ACCESS_FINE_LOCATION
        )==PackageManager.PERMISSION_GRANTED

        if(!isLocationPermissionGranted){
            permissionRequest.add(permission.ACCESS_FINE_LOCATION)
        }

        if(permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    fun Register(view: View) {
        val intent= Intent(this,SignUpActivity::class.java)
        startActivity(intent)
    }
    fun Login(view: View) {
        val intent=Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}
