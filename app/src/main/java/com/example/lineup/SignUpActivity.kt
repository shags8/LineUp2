package com.example.lineup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lineup.RetrofitApi.apiInterface
import com.gdsc.lineup2024.databinding.ActivitySignUpBinding
import com.example.lineup.models.SignUp
import com.example.lineup.models.SignUp2
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var databaseReference = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullname = binding.name
        val Email = binding.email
        val zealid = binding.zeal
        val Password = binding.password
        val Registbutton = binding.regtbtn

        sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
    }
    fun Registration(view: View)
     {
        binding.regtbtn.isEnabled = false
         binding.regText.isEnabled = false
         val fullname = binding.name
         val Email = binding.email
         val zealid = binding.zeal
         val Password = binding.password
         val Registbutton = binding.regtbtn

         val fullnametxt = fullname.text.trim().toString()
         val emailtxt = Email.text.toString()
         val zealidtxt = zealid.text.trim().toString()
         val passwordtxt = Password.text.trim().toString()
         val editor = sharedPreferences.edit()
         editor.putString("Name", fullnametxt)

         progressBar = binding.progressBar

         val userSignUp = SignUp(emailtxt, passwordtxt, fullnametxt, zealidtxt)
         val call = apiInterface.signup(userSignUp)
         if (fullnametxt.isEmpty() || emailtxt.isEmpty() || zealidtxt.isEmpty() || passwordtxt.isEmpty()) {
             Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
             binding.regtbtn.isEnabled = true
             binding.regText.isEnabled = true
         }else if (zealidtxt.length < 6) {
             Toast.makeText(this, "Zeal Id must have 6 characters", Toast.LENGTH_SHORT).show()
             binding.regtbtn.isEnabled = true
             binding.regText.isEnabled = true
         }
          else if (!validateEmail(emailtxt)) {
             Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
             binding.regtbtn.isEnabled = true
             binding.regText.isEnabled = true
         } else {
             showLoading()
             binding.regtbtn.isEnabled = true
             binding.regText.isEnabled = true
             call.enqueue(object : Callback<SignUp2> {
                 override fun onResponse(call: Call<SignUp2>, response: Response<SignUp2>) {
                     if (response.isSuccessful) {
                         val responseBody = response.body()
                         if (responseBody != null) {
                             Log.e("id123", "$responseBody")
                             editor.putString("Token", responseBody.token)
                         }
                         editor.apply()
                         if (responseBody != null) {
                             //  Log.e("id123", "$responseBody")
                             Log.e("id123", "${responseBody.code}")
                             Log.e("id123", responseBody.message)
                             if (responseBody.message == "Signup successful") {
                                 hideLoading()
                                 //  dismissLoadingDialog()
                                 Toast.makeText(
                                     this@SignUpActivity,
                                     "Registered Successfully",
                                     Toast.LENGTH_SHORT
                                 ).show()
                                 val intent =
                                     Intent(this@SignUpActivity, CharacterSelect::class.java)
                                 startActivity(intent)
                                 finish()
                             }
                         }

                     } else {
                         hideLoading()
                         Toast.makeText(
                             this@SignUpActivity,
                             "Zeal Id is already registered",
                             Toast.LENGTH_SHORT
                         ).show()
                         Log.e("id123", "${response.code()} - ${response.message()}")

                     }
                 }

                 override fun onFailure(call: Call<SignUp2>, t: Throwable) {
                     hideLoading()
                     Toast.makeText(
                         this@SignUpActivity, t.message.toString(), Toast.LENGTH_SHORT
                     ).show()
                 }
             })
         }
     }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        binding.signUpRel.visibility = View.GONE
        //overlay.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        //overlay.visibility = View.GONE
        binding.signUpRel.visibility = View.VISIBLE
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }
        return false
    }
}