package com.example.lineup.models

data class scanner(
    val message: String,
    val scannedCodes:ArrayList<String>
)

data class Code(
    val code: String
)