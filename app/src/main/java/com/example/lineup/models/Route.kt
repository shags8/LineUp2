package com.example.lineup.models

data class Route(
 val nearestUsers : Array<location>
)
data class location(
    val name : String,
    var avatar : Int,
    val distance: Double,
    val direction: String
)
