package com.example.lineup.dataClass

data class SignUp(
    val email:String ,
    val password:String ,
    val name:String ,
    val zealId:String ,
)
data class SignUp2(
    val protocol:String,
    val code:Int,
    val url:String,
    val message:String,
    val token:String

)
data class qrCode(
    val code :String
)
