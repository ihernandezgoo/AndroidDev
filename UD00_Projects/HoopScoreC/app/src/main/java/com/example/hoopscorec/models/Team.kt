package com.example.hoopscorec.models

import java.io.Serializable

data class Team(
    val name: String,
    val players: List<Player>
) : Serializable
