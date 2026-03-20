package com.example.neyza_insight.pertemuan_2

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val btnPersegi = findViewById<LinearLayout>(R.id.btnPersegi)
        val btnBalok = findViewById<LinearLayout>(R.id.btnBalok)

        btnPersegi.setOnClickListener {
            startActivity(Intent(this, PersegiPanjangActivity::class.java))
        }

        btnBalok.setOnClickListener {
            startActivity(Intent(this, BalokActivity::class.java))
        }
    }
}