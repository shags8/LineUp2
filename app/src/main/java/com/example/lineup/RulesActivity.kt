package com.example.lineup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.gdsc.lineup2024.databinding.ActivityRulesBinding

class RulesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRulesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("Name", "defaultValue")
        binding.username.text= "Hey $name"
        if (name != null) {
        }
    }

    fun rulesnext(view: View)
    {
        val intent = Intent(this, bottom_activity::class.java)
        startActivity(intent)
        finish()
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
}