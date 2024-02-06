// login up
package com.example.lineup
//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NWMyNTgzYWIyMTY1MmI0ZWEzMDQ4MzIiLCJpYXQiOjE3MDcyMzU2MTgsImV4cCI6MTcwNzIzOTIxOH0.MxeyMifZTJF4G5BfTDf4VhXXGyxRkEN9pj6Uj5hueLQ

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.example.lineup.models.Login
import com.example.lineup.models.Login2
import com.example.lineup.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressBar: ProgressBar
    private lateinit var overlay: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root);

        val loginbtn = binding.loginBtn
        sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue")

        loginbtn.setOnClickListener {
            val zeal = binding.zeal.text.trim().toString()
            val password = binding.password.text.trim().toString()
            progressBar = binding.progressBar
            overlay = binding.overlay

            if (zeal.isEmpty() || password.isEmpty()) {
                    showToast("Please enter your ZealId and Password")
            } else {
                showLoading()
                val userLogin = Login(password, zeal)
                val call = RetrofitApi.apiInterface.login(userLogin)
                call.enqueue(object : Callback<Login2> {
                    override fun onResponse(call: Call<Login2>, response: Response<Login2>) {
                        if (response.isSuccessful) {
                            val bodyReponse = response.body()
                            //    Log.e("id345", "${response.headers()}")
                            Log.e("id1234", "$response")
                            if (bodyReponse != null) {
                                Log.e("id1234", "$bodyReponse")
                                if (bodyReponse.message == "Login successful") {
                                    Log.e("id1", "$retrievedValue")
                                    editor.putString("Token", retrievedValue)
                                    showToast("Login Successfully")
                                    val intent =
                                        Intent(this@LoginActivity, bottom_activity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            hideLoading()
                            showToast("User Not Found!")
                        }
                    }

                    override fun onFailure(call: Call<Login2>, t: Throwable) {
                        hideLoading()
                        showToast("Login Failed")
                    }
                })
            }
        }

    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        overlay.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        overlay.visibility = View.GONE
    }
    private fun showToast( message: String ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}