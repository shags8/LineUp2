package com.example.lineup

import com.example.lineup.models.LeaderboardModel
import com.example.lineup.models.Login
import com.example.lineup.models.Login2
import com.example.lineup.models.SignUp
import com.example.lineup.models.SignUp2
import com.example.lineup.models.qrCode
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

    @GET("leaderboard")
    fun getPlayers(@Header("Authorization") token: String): retrofit2.Call<LeaderboardModel>
}