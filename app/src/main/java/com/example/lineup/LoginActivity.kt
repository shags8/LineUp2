package com.example.lineup
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root);

        val loginbtn = binding.loginBtn
        sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        loginbtn.setOnClickListener {
            val zeal = binding.zeal.text.trim().toString()
            val password = binding.password.text.trim().toString()
            progressBar = binding.progressBar

            if (zeal.isEmpty() || password.isEmpty()) {
                showToast("Please enter your ZealId and Password")
            } else {
                showLoading()
                val userLogin = Login(password, zeal)
                val call = RetrofitApi.apiInterface.login(userLogin)
                call.enqueue(object : Callback<Login2> {
                    override fun onResponse(call: Call<Login2>, response: Response<Login2>) {
                        if (response.isSuccessful) {
                            val bodyResponse = response.body()
                            Log.e("id1234", "$response")
                            if (bodyResponse != null) {
                                Log.e("id1234", "$bodyResponse")
                                if (bodyResponse.message == "Login successful") {
                                    hideLoading()
                                    editor.putString("Token", response.body()!!.token)
                                    editor.apply()
                                    showToast("Login Successfully")
                                    val intent = Intent(this@LoginActivity, CountDownActivity::class.java)
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
        binding.relLayout.visibility=View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        binding.relLayout.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}