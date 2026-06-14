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
import com.example.neyza_insight.data.entity.KematianEntity
import com.example.neyza_insight.databinding.ActivityKematianFormBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class KematianFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKematianFormBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKematianFormBinding.inflate(layoutInflater)
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
            binding.etTanggalMeninggal.setText(sdf.format(calendar.time))
        }

        binding.etTanggalMeninggal.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSaveKematian.setOnClickListener {
            val nama = binding.etNamaKematian.text.toString().trim()
            val nik = binding.etNik.text.toString().trim()
            val noSurat = binding.etNoSurat.text.toString().trim()
            val tanggalMeninggal = binding.etTanggalMeninggal.text.toString().trim()
            val lokasi = binding.etLokasi.text.toString().trim()
            val sebab = binding.etSebab.text.toString().trim()
            val alamat = binding.etAlamat.text.toString().trim()
            var imageUrl = binding.etImageUrl.text.toString().trim()

            val jenisKelamin = if (binding.rbLaki.isChecked) "Laki-laki" else "Perempuan"

            if (nama.isEmpty() || nik.isEmpty() || noSurat.isEmpty() ||
                tanggalMeninggal.isEmpty() || lokasi.isEmpty() || sebab.isEmpty() ||
                alamat.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUrl.isEmpty()) {
                // Default placeholder image
                imageUrl = "https://randomuser.me/api/portraits/lego/2.jpg"
            }

            lifecycleScope.launch {
                val newRecord = KematianEntity(
                    nama = nama,
                    nik = nik,
                    noSurat = noSurat,
                    tanggalMeninggal = tanggalMeninggal,
                    lokasi = lokasi,
                    sebabKematian = sebab,
                    jenisKelamin = jenisKelamin,
                    alamat = alamat,
                    imageUrl = imageUrl
                )
                db.kematianDao().insert(newRecord)
                Toast.makeText(this@KematianFormActivity, "Data Kematian berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
