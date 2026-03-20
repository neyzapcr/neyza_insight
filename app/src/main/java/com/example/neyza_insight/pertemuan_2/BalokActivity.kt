package com.example.neyza_insight.pertemuan_2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.R

class BalokActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balok)

        val inputPanjang = findViewById<EditText>(R.id.inputPanjang)
        val inputLebar = findViewById<EditText>(R.id.inputLebar)
        val inputTinggi = findViewById<EditText>(R.id.inputTinggi)

        val btnHitung = findViewById<Button>(R.id.btnHitung)
        val txtHasil = findViewById<TextView>(R.id.txtHasil)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnHitung.setOnClickListener {

            val p = inputPanjang.text.toString().toDouble()
            val l = inputLebar.text.toString().toDouble()
            val t = inputTinggi.text.toString().toDouble()

            val volume = p * l * t
            val luas = 2 * (p*l + p*t + l*t)

            txtHasil.text =
                "Volume : $volume\nLuas Permukaan : $luas"
        }
    }
}