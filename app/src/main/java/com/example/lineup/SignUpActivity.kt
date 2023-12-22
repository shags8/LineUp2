package com.example.lineup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.lineup.databinding.ActivityLoginBinding
import com.example.lineup.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val zealId=binding.zealId.toString()
       // val zealId=binding.email.toString()

        binding.nextBtn.setOnClickListener{
            val intent=Intent(this,bottom_activity::class.java)
            val fragment=scanner()
            val bundle=Bundle()
            bundle.putString("id",zealId)
            fragment.arguments=bundle
            startActivity(intent)
        }
    }
}