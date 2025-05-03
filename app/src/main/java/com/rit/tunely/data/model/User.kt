package com.rit.tunely.data.model

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val points: Int = 0
)
