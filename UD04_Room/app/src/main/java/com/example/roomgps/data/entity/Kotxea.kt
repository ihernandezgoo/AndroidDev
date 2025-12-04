package com.example.roomgps.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kotxea")
data class Kotxea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val matrikula: String,
    val marka: String,
    val modeloa: String,
    val kolorea: String? = null,
    val urtea: Int? = null
)

