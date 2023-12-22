package com.example.lineup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lineup.databinding.ActivityBottomBinding
import com.example.lineup.databinding.ActivityLoginBinding
import com.example.lineup.bottom_activity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener{
            val i= Intent(this,bottom_activity::class.java)
            startActivity(i)
        }
    }
}