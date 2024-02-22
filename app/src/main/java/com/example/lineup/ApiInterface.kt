package com.example.lineup

import com.example.lineup.models.Avatar
import com.example.lineup.models.Avatar2
import com.example.lineup.models.Code
import com.example.lineup.models.LeaderboardModel2
import com.example.lineup.models.Login
import com.example.lineup.models.Login2
import com.example.lineup.models.Route
import com.example.lineup.models.SignUp
import com.example.lineup.models.SignUp2
import com.example.lineup.models.qrCode
import com.example.lineup.models.scanner
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("user/signup")
    fun signup(@Body signUp: SignUp): Call<SignUp2>

    @POST("user/login")
    fun login(@Body login: Login): Call<Login2>

    @GET("user/generate-qr")
    fun getCode(@Header("Authorization") qrCode: String): Call<qrCode>

    @GET("user/leaderboard")
    fun getPlayers(@Header("Authorization") token: String): Call<LeaderboardModel2>

    @GET("user/refresh-location")
    fun getRoute(@Header("Authorization") token: String): Call<Route>

    @POST("user/store-avatar")
    fun storeAvatar(@Header("Authorization") token: String , @Body avatar:Avatar ): Call<Avatar2>

    @POST("user/scan-qrcode")
    fun scan(@Header("Authorization") token: String, @Body code: Code): Call<scanner>
}