package com.example.lineup

import com.example.lineup.dataClass.Login
import com.example.lineup.dataClass.SignUp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("signup")
    fun signup( @Body signUp: SignUp): Call<SignUp>

    @POST("login")
    fun login( @Body login: Login):Call<Login>
}