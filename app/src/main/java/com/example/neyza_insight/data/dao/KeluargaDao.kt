package com.example.neyza_insight.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.neyza_insight.data.entity.KeluargaEntity

@Dao
interface KeluargaDao {
    @Query("SELECT * FROM keluarga ORDER BY id DESC")
    suspend fun getAll(): List<KeluargaEntity>

    @Query("SELECT * FROM keluarga WHERE noKk LIKE :query OR namaKepalaKeluarga LIKE :query ORDER BY id DESC")
    suspend fun search(query: String): List<KeluargaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keluarga: KeluargaEntity): Long

    @Delete
    suspend fun delete(keluarga: KeluargaEntity)
}
