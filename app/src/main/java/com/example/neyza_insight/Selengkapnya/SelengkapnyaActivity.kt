package com.example.neyza_insight.Selengkapnya

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.ActivitySelengkapnyaBinding

class SelengkapnyaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelengkapnyaBinding

    private val menuList = listOf(
        MenuModel(
            iconRes     = R.drawable.ic_menu_about_app,
            judul       = "Tentang Aplikasi",
            deskripsi   = "Informasi versi dan deskripsi aplikasi Bina Desa"
        ),
        MenuModel(
            iconRes     = R.drawable.ic_menu_desa,
            judul       = "Profil Desa",
            deskripsi   = "Sejarah, visi, dan misi desa kami"
        ),
        MenuModel(
            iconRes     = R.drawable.ic_menu_kontak,
            judul       = "Kontak Desa",
            deskripsi   = "Hubungi perangkat desa secara langsung"
        ),
        MenuModel(
            iconRes     = R.drawable.ic_menu_bantuan,
            judul       = "Bantuan",
            deskripsi   = "Panduan penggunaan dan FAQ aplikasi"
        ),
        MenuModel(
            iconRes     = R.drawable.ic_menu_privacy,
            judul       = "Privacy Policy",
            deskripsi   = "Kebijakan privasi dan keamanan data pengguna"
        ),
        MenuModel(
            iconRes     = R.drawable.ic_menu_kritik,
            judul       = "Kritik & Saran",
            deskripsi   = "Sampaikan masukan untuk pengembangan aplikasi"
        ),
        MenuModel(
            iconRes     = R.drawable.ic_menu_versi,
            judul       = "Versi Aplikasi",
            deskripsi   = "Bina Desa v1.0.0 — Build 2024"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelengkapnyaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Setup adapter
        val adapter = SelengkapnyaAdapter(this, menuList)
        binding.listViewMenu.adapter = adapter

        // Klik item
        binding.listViewMenu.setOnItemClickListener { _, _, position, _ ->
            val item = menuList[position]
            Toast.makeText(this, item.judul, Toast.LENGTH_SHORT).show()
        }
    }
}
