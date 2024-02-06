package com.example.lineup.models

data class LeaderboardModel(
    var name: String,
    var membersFound: Int,
    var avatar: Int,
)
data class LeaderboardModel2(
    var users:List<LeaderboardModel>
)
