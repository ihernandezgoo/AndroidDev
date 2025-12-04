package com.example.roomgps.data.dao

import androidx.room.*
import com.example.roomgps.data.entity.Gps
import kotlinx.coroutines.flow.Flow

@Dao
interface GpsDao {
    @Insert
    suspend fun insert(gps: Gps)

    @Update
    suspend fun update(gps: Gps)

    @Delete
    suspend fun delete(gps: Gps)

    @Query("SELECT * FROM gps ORDER BY timestamp DESC")
    fun getAllGps(): Flow<List<Gps>>

    @Query("SELECT * FROM gps WHERE id = :id")
    suspend fun getGpsById(id: Int): Gps?
}

