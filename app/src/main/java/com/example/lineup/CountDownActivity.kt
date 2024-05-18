package com.example.lineup

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.lineup2024.databinding.ActivityCountDownBinding
import com.example.lineup.models.Timer
import com.gdsc.lineup2024.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class CountDownActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountDownBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var countDownTimer: CountDownTimer
    private val COUNTDOWN_TIME: Long = 60000
    private var timeLeftInMillis: Long = COUNTDOWN_TIME
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountDownBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = binding.progressBar


        sharedPreferences =
            this.getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue")
        val retrievedValue2 = sharedPreferences.getString("Character Token", "defaultValue")
        val header = "Bearer $retrievedValue"
        val call = RetrofitApi.apiInterface.getTimeLeft(header)
        showLoading()
        call.enqueue(object : Callback<Timer> {
            override fun onResponse(call: Call<Timer>, response: Response<Timer>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    hideLoading()
                    val timeLeft = responseBody.remainingTimeInMilliseconds.toInt()
                    timeLeftInMillis = timeLeft.toLong()
                    if (timeLeftInMillis <= 0) {
                        // If time is already zero, show the button and text immediately
                        binding.text3.visibility = View.VISIBLE
                        binding.gameStart.visibility = View.VISIBLE
                        binding.countdownTimer.visibility = View.GONE
                        binding.text2.visibility = View.GONE
                        binding.text1.visibility = View.GONE
                    } else {
                        // If time is not zero, start the timer
                        startTimer()
                    }
                }
            }

            override fun onFailure(call: Call<Timer>, t: Throwable) {
                Toast.makeText(this@CountDownActivity,"Something Went Wrong!",Toast.LENGTH_SHORT).show()
                hideLoading()
            }

        })
        binding.gameStart.setOnClickListener {
            if(retrievedValue2 != null &&retrievedValue2!="defaultValue")
            {
                val intent = Intent(this@CountDownActivity, RulesActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                val intent = Intent(this@CountDownActivity, CharacterSelect::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun startTimer(){

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }
            override fun onFinish() {
                timeLeftInMillis = 0
                updateCountDownText()
            }
        }.start()
    }
    private fun updateCountDownText() {
        val days = TimeUnit.MILLISECONDS.toDays(timeLeftInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(timeLeftInMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % 60

            // Timer is still running, continue updating the countdown text
            val timeLeftFormatted = when {
                days > 0 -> String.format("%d days %02d hours", days, hours)
                hours > 0 -> String.format("%02d hours %02d minutes", hours, minutes)
                minutes > 0 -> String.format("%02d minutes %02d seconds", minutes, seconds)
                else -> String.format("%02d seconds", seconds)
            }
            binding.countdownTimer.text = timeLeftFormatted

    }
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        binding.constRel.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        binding.constRel.visibility = View.VISIBLE
    }
}