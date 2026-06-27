package com.example.neyza_insight.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kelahiran")
data class KelahiranEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val noAkta: String,
    val tanggalLahir: String,
    val tempatLahir: String,
    val namaAyah: String,
    val namaIbu: String,
    val imageUrl: String,
    val status: String = "Selesai"
)
