package com.example.neyza_insight.Home.pertemuan_10

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.neyza_insight.R
import com.example.neyza_insight.data.AppDatabase
import com.example.neyza_insight.data.entity.PindahanEntity
import com.example.neyza_insight.databinding.ActivityPindahanFormBinding
import com.example.neyza_insight.reminder.ReminderDialogHelper
import com.example.neyza_insight.reminder.ReminderHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PindahanFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPindahanFormBinding
    private lateinit var db: AppDatabase
    private var currentDraftId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPindahanFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        // Set up Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Detect Edit Mode
        val draftId = intent.getIntExtra("EXTRA_DRAFT_ID", 0)
        if (draftId > 0) {
            currentDraftId = draftId
            supportActionBar?.title = "Edit Draft Perpindahan"
            loadDraftData()
        }

        // Set up Date Picker
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.etTanggalPindah.setText(sdf.format(calendar.time))
        }

        binding.etTanggalPindah.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Save Draft Action
        binding.btnSaveDraft.setOnClickListener {
            val nama = binding.etNamaPindahan.text.toString().trim()
            if (nama.isEmpty()) {
                binding.etNamaPindahan.error = "Nama wajib diisi untuk menyimpan draft"
                return@setOnClickListener
            }

            val nik = binding.etNik.text.toString().trim()
            val noSuratPindah = binding.etNoSuratPindah.text.toString().trim()
            val tanggalPindah = binding.etTanggalPindah.text.toString().trim()
            val alamatAsal = binding.etAlamatAsal.text.toString().trim()
            val alamatTujuan = binding.etAlamatTujuan.text.toString().trim()
            val alasanPindah = binding.etAlasanPindah.text.toString().trim()
            var imageUrl = binding.etImageUrl.text.toString().trim()

            val jenisKelamin = if (binding.rbLaki.isChecked) "Laki-laki" else "Perempuan"

            if (imageUrl.isEmpty()) {
                imageUrl = "https://randomuser.me/api/portraits/lego/3.jpg"
            }

            lifecycleScope.launch {
                val record = PindahanEntity(
                    id = currentDraftId,
                    nama = nama,
                    nik = nik,
                    noSuratPindah = noSuratPindah,
                    tanggalPindah = tanggalPindah,
                    alamatAsal = alamatAsal,
                    alamatTujuan = alamatTujuan,
                    alasanPindah = alasanPindah,
                    jenisKelamin = jenisKelamin,
                    imageUrl = imageUrl,
                    status = "Draft"
                )
                val insertedId = db.pindahanDao().insert(record)
                currentDraftId = insertedId.toInt()

                val resultIntent = android.content.Intent().apply {
                    putExtra("SHOW_SNACKBAR", true)
                    putExtra("DRAFT_ID", currentDraftId)
                    putExtra("EVENT_TYPE", "pindahan")
                }
                setResult(android.app.Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        // Save Complete Action
        binding.btnSavePindahan.setOnClickListener {
            val nama = binding.etNamaPindahan.text.toString().trim()
            val nik = binding.etNik.text.toString().trim()
            val noSuratPindah = binding.etNoSuratPindah.text.toString().trim()
            val tanggalPindah = binding.etTanggalPindah.text.toString().trim()
            val alamatAsal = binding.etAlamatAsal.text.toString().trim()
            val alamatTujuan = binding.etAlamatTujuan.text.toString().trim()
            val alasanPindah = binding.etAlasanPindah.text.toString().trim()
            var imageUrl = binding.etImageUrl.text.toString().trim()

            val jenisKelamin = if (binding.rbLaki.isChecked) "Laki-laki" else "Perempuan"

            if (nama.isEmpty() || nik.isEmpty() || noSuratPindah.isEmpty() ||
                tanggalPindah.isEmpty() || alamatAsal.isEmpty() || alamatTujuan.isEmpty() ||
                alasanPindah.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUrl.isEmpty()) {
                imageUrl = "https://randomuser.me/api/portraits/lego/3.jpg"
            }

            lifecycleScope.launch {
                val record = PindahanEntity(
                    id = currentDraftId,
                    nama = nama,
                    nik = nik,
                    noSuratPindah = noSuratPindah,
                    tanggalPindah = tanggalPindah,
                    alamatAsal = alamatAsal,
                    alamatTujuan = alamatTujuan,
                    alasanPindah = alasanPindah,
                    jenisKelamin = jenisKelamin,
                    imageUrl = imageUrl,
                    status = "Selesai"
                )
                db.pindahanDao().insert(record)
                Toast.makeText(this@PindahanFormActivity, "Data Perpindahan berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun loadDraftData() {
        lifecycleScope.launch {
            val data = db.pindahanDao().getAll()
            val draft = data.find { it.id == currentDraftId }
            draft?.let {
                binding.etNamaPindahan.setText(it.nama)
                binding.etNik.setText(it.nik)
                binding.etNoSuratPindah.setText(it.noSuratPindah)
                binding.etTanggalPindah.setText(it.tanggalPindah)
                binding.etAlamatAsal.setText(it.alamatAsal)
                binding.etAlamatTujuan.setText(it.alamatTujuan)
                binding.etAlasanPindah.setText(it.alasanPindah)
                binding.etImageUrl.setText(it.imageUrl)
                if (it.jenisKelamin == "Laki-laki") {
                    binding.rbLaki.isChecked = true
                } else {
                    binding.rbPerempuan.isChecked = true
                }
            }
        }
    }
}
