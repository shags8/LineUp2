package com.example.lineup.models

data class Login(
    val password: String,
    val zealId: String
)

data class Login2(
    val protocol: String,
    val code: String,
    val token :String,
    val message: String,
    val url: String,
)
