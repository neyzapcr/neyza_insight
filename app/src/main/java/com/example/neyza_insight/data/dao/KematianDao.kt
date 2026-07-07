package com.example.neyza_insight.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.neyza_insight.data.entity.KematianEntity

@Dao
interface KematianDao {
    @Query("SELECT * FROM kematian ORDER BY id DESC")
    suspend fun getAll(): List<KematianEntity>

    @Query("SELECT * FROM kematian WHERE id = :id")
    suspend fun getById(id: Int): KematianEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kematian: KematianEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<KematianEntity>)

    @Delete
    suspend fun delete(kematian: KematianEntity)
}
