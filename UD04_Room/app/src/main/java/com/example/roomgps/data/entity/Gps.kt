package com.example.roomgps.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gps")
data class Gps(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val destinoNombre: String,
    val timestamp: Long = System.currentTimeMillis()
)

