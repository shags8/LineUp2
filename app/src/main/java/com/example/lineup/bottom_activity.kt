package com.example.lineup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lineup.databinding.ActivityBottomBinding


class bottom_activity : AppCompatActivity() {

    private lateinit var binding : ActivityBottomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBottomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragments(Qr_code())

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

    private fun replaceFragments(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }


    }
