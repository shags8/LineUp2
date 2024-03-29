package com.example.lineup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lineup.RetrofitApi.apiInterface
import com.example.lineup.databinding.ActivitySignUpBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullname = binding.name
        val Email = binding.email
        val zealid = binding.zeal
        val Password = binding.password
        val Registbutton = binding.regtbtn

        val sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        Registbutton.setOnClickListener {

            val fullnametxt = fullname.text.trim().toString()
            val emailtxt = Email.text.toString()
            val zealidtxt = zealid.text.trim().toString()
            val passwordtxt = Password.text.trim().toString()

            editor.putString("Name" , fullnametxt)

            progressBar=binding.progressBar

            val userSignUp = SignUp(emailtxt, passwordtxt, fullnametxt, zealidtxt)
            val call = apiInterface.signup(userSignUp)
            if (fullnametxt.isEmpty() || emailtxt.isEmpty() || zealidtxt.isEmpty() || passwordtxt.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                showLoading()
                // Log.e("id123", "hey")
                call.enqueue(object : Callback<SignUp2> {
                    override fun onResponse(call: Call<SignUp2>, response: Response<SignUp2>) {
                        //    showLoadingDialog(this@SignUpActivity,"Loading...")
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
    }
//    fun showLoadingDialog(context: Context, message: String) {
//
//            val builder = AlertDialog.Builder(context)
//            builder.setMessage(message)
//            builder.setCancelable(false)
//            alertDialog = builder.create()
//            alertDialog?.show()
//
//    }
//
//    // Function to dismiss the loading dialog
//    fun dismissLoadingDialog() {
//        alertDialog?.let {
//            if (it.isShowing) {
//                it.dismiss()
//            }
//            alertDialog = null // Reset to null after dismissing
//        }
//    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        binding.signUpRel.visibility=View.GONE
        //overlay.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        //overlay.visibility = View.GONE
        binding.signUpRel.visibility=View.VISIBLE

    }
}