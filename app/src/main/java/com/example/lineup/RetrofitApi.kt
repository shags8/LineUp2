package com.example.lineup

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://lineup-backend.onrender.com/")
            .build()
    }

    val apiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}