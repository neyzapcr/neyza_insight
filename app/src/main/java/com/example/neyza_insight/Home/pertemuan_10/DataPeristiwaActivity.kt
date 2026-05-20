package com.example.neyza_insight.Home.pertemuan_10

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.ActivityDataPeristiwaBinding
import com.google.android.material.tabs.TabLayoutMediator

class DataPeristiwaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPeristiwaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding
        binding = ActivityDataPeristiwaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Toolbar (jika ada butang back)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 1. Inisialisasi Adapter yang baru dibuat
        val peristiwaAdapter = DataPeristiwaAdapter(this)

        // 2. Set adapter ke ViewPager2
        binding.viewPager.adapter = peristiwaAdapter

        // 3. Hubungkan TabLayout & ViewPager2 menggunakan TabLayoutMediator
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // Mengatur judul bagi setiap tab peristiwa
            when (position) {
                0 -> tab.text = "Kelahiran"
                1 -> tab.text = "Kematian"
                2 -> tab.text = "Perpindahan"
            }
        }.attach()
    }
}