package com.example.neyza_insight.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pindahan")
data class PindahanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val noSuratPindah: String,
    val tanggalPindah: String,
    val alamatTujuan: String,
    val alasanPindah: String,
    val nik: String,
    val jenisKelamin: String,
    val alamatAsal: String,
    val imageUrl: String,
    val status: String = "Selesai"
)
