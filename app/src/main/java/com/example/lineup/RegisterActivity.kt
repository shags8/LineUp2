package com.example.lineup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.lineup.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var flag=intent.getIntExtra("Register",-1)
        if(flag==1) {
            binding.regText.text = "Register"
            replaceFragment(RegisterFragment())
        }
        if(flag==0){
            binding.regText.text="Login"
            replaceFragment(LoginFragment())
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fm:FragmentManager=supportFragmentManager
        val ft:FragmentTransaction=fm.beginTransaction()
        ft.replace(R.id.frag_container,fragment)
        ft.commit()
    }
}