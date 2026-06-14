package com.example.neyza_insight.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kematian")
data class KematianEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val noSurat: String,
    val tanggalMeninggal: String,
    val lokasi: String,
    val sebabKematian: String,
    val nik: String,
    val jenisKelamin: String,
    val alamat: String,
    val imageUrl: String
)
