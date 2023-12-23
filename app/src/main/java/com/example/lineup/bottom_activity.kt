package com.example.lineup

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lineup.databinding.ActivityBottomBinding


class bottom_activity : AppCompatActivity() {

    private lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>
    private var isCameraPermissionGranted=false
    val permissionRequest= mutableListOf<String>()

    private lateinit var binding : ActivityBottomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBottomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        replaceFragments(Qr_code())


        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permissions->
            isCameraPermissionGranted= permissions[Manifest.permission.CAMERA]?:isCameraPermissionGranted
            if (!isCameraPermissionGranted ) {
                showPermissionDeniedDialog()
            }

        }
        requestPermission()


        val bottomNavBar = binding.bottomNavigationView
        bottomNavBar.itemIconTintList = null
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
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

    private fun requestPermission(){
        isCameraPermissionGranted=ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )==PackageManager.PERMISSION_GRANTED

        if(!isCameraPermissionGranted){
            permissionRequest.add(Manifest.permission.CAMERA)
        }

        if(permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    private fun replaceFragments(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
    override fun onBackPressed() {
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
                super.onBackPressed()
            })
            .show()
    }
    }
