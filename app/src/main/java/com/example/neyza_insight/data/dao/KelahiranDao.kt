package com.example.neyza_insight.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.neyza_insight.data.entity.KelahiranEntity

@Dao
interface KelahiranDao {
    @Query("SELECT * FROM kelahiran ORDER BY id DESC")
    suspend fun getAll(): List<KelahiranEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kelahiran: KelahiranEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<KelahiranEntity>)

    @Delete
    suspend fun delete(kelahiran: KelahiranEntity)
}
