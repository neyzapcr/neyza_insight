package com.example.neyza_insight.Keluarga

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.neyza_insight.data.AppDatabase
import com.example.neyza_insight.data.entity.KeluargaEntity
import com.example.neyza_insight.databinding.ActivityKeluargaFormBinding
import kotlinx.coroutines.launch

class KeluargaFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeluargaFormBinding
    private lateinit var db: AppDatabase
    private var keluargaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeluargaFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Detect Edit Mode
        keluargaId = intent.getIntExtra("EXTRA_KELUARGA_ID", 0)
        if (keluargaId > 0) {
            supportActionBar?.title = "Edit Data Keluarga"
            loadKeluargaData()
        }

        binding.btnSave.setOnClickListener {
            saveKeluarga()
        }
    }

    private fun loadKeluargaData() {
        lifecycleScope.launch {
            val allData = db.keluargaDao().getAll()
            val keluarga = allData.find { it.id == keluargaId }
            keluarga?.let {
                binding.etNoKk.setText(it.noKk)
                binding.etNamaKepalaKeluarga.setText(it.namaKepalaKeluarga)
                binding.etAlamat.setText(it.alamat)
                binding.etRtRw.setText(it.rtRw)
                binding.etDusun.setText(it.dusun)
                binding.etJumlahAnggota.setText(it.jumlahAnggota.toString())
            }
        }
    }

    private fun saveKeluarga() {
        val noKk = binding.etNoKk.text.toString().trim()
        val namaKepala = binding.etNamaKepalaKeluarga.text.toString().trim()
        val alamat = binding.etAlamat.text.toString().trim()
        val rtRw = binding.etRtRw.text.toString().trim()
        val dusun = binding.etDusun.text.toString().trim()
        val jumlahAnggotaStr = binding.etJumlahAnggota.text.toString().trim()

        if (noKk.isEmpty()) {
            binding.etNoKk.error = "Nomor KK wajib diisi"
            return
        }
        if (namaKepala.isEmpty()) {
            binding.etNamaKepalaKeluarga.error = "Nama Kepala Keluarga wajib diisi"
            return
        }
        if (alamat.isEmpty()) {
            binding.etAlamat.error = "Alamat wajib diisi"
            return
        }
        if (rtRw.isEmpty()) {
            binding.etRtRw.error = "RT/RW wajib diisi"
            return
        }
        if (dusun.isEmpty()) {
            binding.etDusun.error = "Dusun wajib diisi"
            return
        }
        if (jumlahAnggotaStr.isEmpty()) {
            binding.etJumlahAnggota.error = "Jumlah anggota wajib diisi"
            return
        }

        val jumlahAnggota = jumlahAnggotaStr.toIntOrNull()
        if (jumlahAnggota == null || jumlahAnggota < 1) {
            binding.etJumlahAnggota.error = "Jumlah anggota harus minimal 1"
            return
        }

        lifecycleScope.launch {
            val keluarga = KeluargaEntity(
                id = keluargaId,
                noKk = noKk,
                namaKepalaKeluarga = namaKepala,
                alamat = alamat,
                rtRw = rtRw,
                dusun = dusun,
                jumlahAnggota = jumlahAnggota
            )
            db.keluargaDao().insert(keluarga)
            Toast.makeText(this@KeluargaFormActivity, "Data Keluarga berhasil disimpan!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        }
    }
}
