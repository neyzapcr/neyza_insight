package com.example.neyza_insight.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keluarga")
data class KeluargaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val noKk: String,
    val namaKepalaKeluarga: String,
    val alamat: String,
    val rtRw: String,
    val dusun: String,
    val jumlahAnggota: Int
)
