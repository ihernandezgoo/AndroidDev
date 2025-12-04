package com.example.roomgps.data.dao

import androidx.room.*
import com.example.roomgps.data.entity.Kotxea
import kotlinx.coroutines.flow.Flow

@Dao
interface KotxeaDao {
    @Insert
    suspend fun insert(kotxea: Kotxea)

    @Update
    suspend fun update(kotxea: Kotxea)

    @Delete
    suspend fun delete(kotxea: Kotxea)

    @Query("SELECT * FROM kotxea")
    fun getAllKotxeak(): Flow<List<Kotxea>>

    @Query("SELECT * FROM kotxea WHERE id = :id")
    suspend fun getKotxeaById(id: Int): Kotxea?

    @Query("SELECT * FROM kotxea WHERE matrikula = :matrikula")
    suspend fun getKotxeaByMatrikula(matrikula: String): Kotxea?
}

