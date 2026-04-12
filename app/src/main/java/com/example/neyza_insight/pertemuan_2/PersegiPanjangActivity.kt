package com.example.neyza_insight.pertemuan_2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.R

class PersegiPanjangActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persegi_panjang)

        val inputPanjang = findViewById<EditText>(R.id.inputPanjang)
        val inputLebar = findViewById<EditText>(R.id.inputLebar)
        val btnHitung = findViewById<Button>(R.id.btnHitung)
        val txtHasil = findViewById<TextView>(R.id.txtHasil)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnHitung.setOnClickListener {
            val pText = inputPanjang.text.toString().trim()
            val lText = inputLebar.text.toString().trim()

            if (pText.isEmpty() || lText.isEmpty()) {
                Toast.makeText(this, "Semua input wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val p = pText.toDouble()
            val l = lText.toDouble()

            val keliling = 2 * (p + l)
            val luas = p * l

            txtHasil.text = "Keliling : $keliling\nLuas : $luas"
        }
    }
}