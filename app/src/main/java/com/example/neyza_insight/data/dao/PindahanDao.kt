package com.example.neyza_insight.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.neyza_insight.data.entity.PindahanEntity

@Dao
interface PindahanDao {
    @Query("SELECT * FROM pindahan ORDER BY id DESC")
    suspend fun getAll(): List<PindahanEntity>

    @Query("SELECT * FROM pindahan WHERE id = :id")
    suspend fun getById(id: Int): PindahanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pindahan: PindahanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PindahanEntity>)

    @Delete
    suspend fun delete(pindahan: PindahanEntity)
}