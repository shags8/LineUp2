package com.example.lineup

import android.telecom.Call
import com.example.lineup.dataClass.Login
import com.example.lineup.dataClass.SignUp
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("signup")
    fun signup( @Body signUp: SignUp): retrofit2.Call<SignUp>

    @POST("login")
    fun login( @Body login: Login): retrofit2.Call<Login>
}