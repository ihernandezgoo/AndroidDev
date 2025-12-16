package com.example.hoopscorec.models

import java.io.Serializable

data class Player(
    val name: String,
    var points: Int = 0,
    var fouls: Int = 0
) : Serializable
