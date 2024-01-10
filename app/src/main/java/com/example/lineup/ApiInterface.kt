package com.example.lineup

import android.telecom.Call
import com.example.lineup.dataClass.Login
import com.example.lineup.dataClass.Login2
import com.example.lineup.dataClass.SignUp
import com.example.lineup.dataClass.SignUp2
import com.example.lineup.dataClass.qrCode
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("signup")
    fun signup(@Body signUp: SignUp): retrofit2.Call<SignUp2>

    @POST("login")
    fun login(@Body login: Login): retrofit2.Call<Login2>

    @GET("generate-qr")
    fun getCode(@Header("Authorization") qrCode: String): retrofit2.Call<qrCode>
}