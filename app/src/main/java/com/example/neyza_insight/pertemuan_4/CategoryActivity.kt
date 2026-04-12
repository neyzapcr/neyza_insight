package com.example.neyza_insight.pertemuan_4

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("onCreate", "CategoryActivity dibuat pertama kali")

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pageTitle = intent.getStringExtra("page_title") ?: "Welcome User!"
        val pageDesc = intent.getStringExtra("page_desc") ?: "Siap belajar hari ini?"

        val name = intent.getStringExtra("name")
        val from = intent.getStringExtra("from")
        val age = intent.getIntExtra("age", 0)

        Log.e("Data Intent", "Nama: $name , Usia: $age, Asal: $from")

        binding.tvHeaderTitle.text = pageTitle
        binding.tvHeaderDesc.text = pageDesc

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("onStart", "onStart: CategoryActivity terlihat di layar")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy", "CategoryActivity dihapus dari stack")
    }
}