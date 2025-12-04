package com.example.roomgps.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ibilbidea")
data class Ibilbidea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val origenLatitude: Double,
    val origenLongitude: Double,
    val destinoLatitude: Double,
    val destinoLongitude: Double,
    val distancia: Float,
    val duracion: Long,
    val fechaInicio: Long,
    val fechaFin: Long? = null
)

