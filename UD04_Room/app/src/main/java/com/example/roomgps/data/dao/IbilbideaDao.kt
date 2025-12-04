package com.example.roomgps.data.dao

import androidx.room.*
import com.example.roomgps.data.entity.Ibilbidea
import kotlinx.coroutines.flow.Flow

@Dao
interface IbilbideaDao {
    @Insert
    suspend fun insert(ibilbidea: Ibilbidea)

    @Update
    suspend fun update(ibilbidea: Ibilbidea)

    @Delete
    suspend fun delete(ibilbidea: Ibilbidea)

    @Query("SELECT * FROM ibilbidea ORDER BY fechaInicio DESC")
    fun getAllIbilbideak(): Flow<List<Ibilbidea>>

    @Query("SELECT * FROM ibilbidea WHERE id = :id")
    suspend fun getIbilbideaById(id: Int): Ibilbidea?
}

