package com.example.neyza_insight.Home.pertemuan_10

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.neyza_insight.R
import com.example.neyza_insight.data.AppDatabase
import com.example.neyza_insight.data.entity.KelahiranEntity
import com.example.neyza_insight.databinding.ActivityKelahiranFormBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class KelahiranFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKelahiranFormBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelahiranFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Database
        db = AppDatabase.getInstance(this)

        // Set up Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Set up Date Picker
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.etTanggalLahir.setText(sdf.format(calendar.time))
        }

        binding.etTanggalLahir.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSaveKelahiran.setOnClickListener {
            val nama = binding.etNamaBayi.text.toString().trim()
            val noAkta = binding.etNoAkta.text.toString().trim()
            val tanggalLahir = binding.etTanggalLahir.text.toString().trim()
            val tempatLahir = binding.etTempatLahir.text.toString().trim()
            val namaAyah = binding.etNamaAyah.text.toString().trim()
            val namaIbu = binding.etNamaIbu.text.toString().trim()
            var imageUrl = binding.etImageUrl.text.toString().trim()

            if (nama.isEmpty() || noAkta.isEmpty() || tanggalLahir.isEmpty() ||
                tempatLahir.isEmpty() || namaAyah.isEmpty() || namaIbu.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUrl.isEmpty()) {
                // Default placeholder image
                imageUrl = "https://randomuser.me/api/portraits/lego/1.jpg"
            }

            lifecycleScope.launch {
                val newRecord = KelahiranEntity(
                    nama = nama,
                    noAkta = noAkta,
                    tanggalLahir = tanggalLahir,
                    tempatLahir = tempatLahir,
                    namaAyah = namaAyah,
                    namaIbu = namaIbu,
                    imageUrl = imageUrl
                )
                db.kelahiranDao().insert(newRecord)
                Toast.makeText(this@KelahiranFormActivity, "Data Kelahiran berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
