package com.example.neyza_insight.pertemuan_4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.databinding.ActivityDashboardBinding
import com.example.neyza_insight.pertemuan_2.MainActivity
import com.example.neyza_insight.pertemuan_3.LoginActivity
import com.example.neyza_insight.pertemuan_4.CategoryActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("onCreate", "DashboardActivity dibuat pertama kali")

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME") ?: "User"
        val pageTitle = "Welcome $username!"
        val pageDesc = "Siap belajar hari ini?"

        binding.tvWelcome.text = pageTitle
        binding.tvDesc.text = pageDesc

        binding.btnRumusRuang.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("page_title", pageTitle)
            intent.putExtra("page_desc", pageDesc)
            startActivity(intent)
        }

        binding.btnCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("page_title", pageTitle)
            intent.putExtra("page_desc", pageDesc)

            intent.putExtra("name", "Neyza")
            intent.putExtra("from", "Pekanbaru")
            intent.putExtra("age", 20)

            startActivity(intent)
        }

        binding.btnFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            intent.putExtra("page_title", pageTitle)
            intent.putExtra("page_desc", pageDesc)

            intent.putExtra("name", "Neyza")
            intent.putExtra("from", "Pekanbaru")
            intent.putExtra("age", 20)

            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, _ ->
                    dialog.dismiss()
                    Log.e("Info Dialog", "Anda memilih Ya!")

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                    Log.e("Info Dialog", "Anda memilih Tidak!")

                    Snackbar.make(binding.root, "Logout dibatalkan", Snackbar.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("onStart", "onStart: DashboardActivity terlihat di layar")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy", "DashboardActivity dihapus dari stack")
    }
}