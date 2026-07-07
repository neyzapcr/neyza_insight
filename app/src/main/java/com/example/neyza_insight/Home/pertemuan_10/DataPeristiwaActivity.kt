package com.example.neyza_insight.Home.pertemuan_10

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.ActivityDataPeristiwaBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class DataPeristiwaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPeristiwaBinding

    private val scanLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val qrValue = result.data?.getStringExtra("EXTRA_SCAN_RESULT")
            if (qrValue != null) {
                handleScannedQr(qrValue)
            }
        }
    }

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

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_data_peristiwa, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_scan -> {
                val intent = android.content.Intent(this, ScanQrActivity::class.java)
                scanLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleScannedQr(qrValue: String) {
        val parts = qrValue.split(":")
        if (parts.size != 2) {
            android.widget.Toast.makeText(this, "Format QR Code tidak valid: $qrValue", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        val type = parts[0]
        val id = parts[1].toIntOrNull()
        if (id == null) {
            android.widget.Toast.makeText(this, "ID tidak valid: ${parts[1]}", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val db = com.example.neyza_insight.data.AppDatabase.getInstance(this@DataPeristiwaActivity)
            when (type) {
                "kelahiran" -> {
                    val entity = db.kelahiranDao().getById(id)
                    if (entity != null) {
                        showKelahiranDetail(entity)
                    } else {
                        android.widget.Toast.makeText(this@DataPeristiwaActivity, "Data Kelahiran tidak ditemukan", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
                "kematian" -> {
                    val entity = db.kematianDao().getById(id)
                    if (entity != null) {
                        showKematianDetail(entity)
                    } else {
                        android.widget.Toast.makeText(this@DataPeristiwaActivity, "Data Kematian tidak ditemukan", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
                "pindahan" -> {
                    val entity = db.pindahanDao().getById(id)
                    if (entity != null) {
                        showPindahanDetail(entity)
                    } else {
                        android.widget.Toast.makeText(this@DataPeristiwaActivity, "Data Perpindahan tidak ditemukan", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    android.widget.Toast.makeText(this@DataPeristiwaActivity, "Jenis data tidak dikenal: $type", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showKelahiranDetail(entity: com.example.neyza_insight.data.entity.KelahiranEntity) {
        val dialogView = android.view.LayoutInflater.from(this).inflate(R.layout.dialog_detail_peristiwa, null)
        val tvTitle = dialogView.findViewById<android.widget.TextView>(R.id.tvTitle)
        val tvDetails = dialogView.findViewById<android.widget.TextView>(R.id.tvDetails)
        val imgAttachment = dialogView.findViewById<android.widget.ImageView>(R.id.imgAttachment)
        val imgQrCode = dialogView.findViewById<android.widget.ImageView>(R.id.imgQrCode)

        tvTitle.text = "Detail Peristiwa Kelahiran"
        tvDetails.text = 
            "Nama Bayi      : ${entity.nama}\n" +
            "No. Akta       : ${entity.noAkta}\n" +
            "Tanggal Lahir  : ${entity.tanggalLahir}\n" +
            "Tempat Lahir   : ${entity.tempatLahir}\n" +
            "Nama Ayah      : ${entity.namaAyah}\n" +
            "Nama Ibu       : ${entity.namaIbu}\n" +
            "Status         : ${entity.status}"

        com.bumptech.glide.Glide.with(this)
            .load(entity.imageUrl)
            .placeholder(R.drawable.ic_document)
            .error(R.drawable.ic_document)
            .into(imgAttachment)

        try {
            val qrBitmap = com.example.neyza_insight.data.QrCodeHelper.createQR("kelahiran:${entity.id}")
            imgQrCode.setImageBitmap(qrBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showKematianDetail(entity: com.example.neyza_insight.data.entity.KematianEntity) {
        val dialogView = android.view.LayoutInflater.from(this).inflate(R.layout.dialog_detail_peristiwa, null)
        val tvTitle = dialogView.findViewById<android.widget.TextView>(R.id.tvTitle)
        val tvDetails = dialogView.findViewById<android.widget.TextView>(R.id.tvDetails)
        val imgAttachment = dialogView.findViewById<android.widget.ImageView>(R.id.imgAttachment)
        val imgQrCode = dialogView.findViewById<android.widget.ImageView>(R.id.imgQrCode)

        tvTitle.text = "Detail Peristiwa Kematian"
        tvDetails.text = 
            "Nama           : ${entity.nama}\n" +
            "NIK            : ${entity.nik}\n" +
            "No. Surat      : ${entity.noSurat}\n" +
            "Tgl Meninggal  : ${entity.tanggalMeninggal}\n" +
            "Lokasi         : ${entity.lokasi}\n" +
            "Sebab Kematian : ${entity.sebabKematian}\n" +
            "Jenis Kelamin  : ${entity.jenisKelamin}\n" +
            "Alamat         : ${entity.alamat}\n" +
            "Status         : ${entity.status}"

        com.bumptech.glide.Glide.with(this)
            .load(entity.imageUrl)
            .placeholder(R.drawable.ic_document)
            .error(R.drawable.ic_document)
            .into(imgAttachment)

        try {
            val qrBitmap = com.example.neyza_insight.data.QrCodeHelper.createQR("kematian:${entity.id}")
            imgQrCode.setImageBitmap(qrBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showPindahanDetail(entity: com.example.neyza_insight.data.entity.PindahanEntity) {
        val dialogView = android.view.LayoutInflater.from(this).inflate(R.layout.dialog_detail_peristiwa, null)
        val tvTitle = dialogView.findViewById<android.widget.TextView>(R.id.tvTitle)
        val tvDetails = dialogView.findViewById<android.widget.TextView>(R.id.tvDetails)
        val imgAttachment = dialogView.findViewById<android.widget.ImageView>(R.id.imgAttachment)
        val imgQrCode = dialogView.findViewById<android.widget.ImageView>(R.id.imgQrCode)

        tvTitle.text = "Detail Peristiwa Perpindahan"
        tvDetails.text = 
            "Nama           : ${entity.nama}\n" +
            "NIK            : ${entity.nik}\n" +
            "No. Surat      : ${entity.noSuratPindah}\n" +
            "Tgl Pindah     : ${entity.tanggalPindah}\n" +
            "Alamat Asal    : ${entity.alamatAsal}\n" +
            "Alamat Tujuan  : ${entity.alamatTujuan}\n" +
            "Alasan Pindah  : ${entity.alasanPindah}\n" +
            "Jenis Kelamin  : ${entity.jenisKelamin}\n" +
            "Status         : ${entity.status}"

        com.bumptech.glide.Glide.with(this)
            .load(entity.imageUrl)
            .placeholder(R.drawable.ic_document)
            .error(R.drawable.ic_document)
            .into(imgAttachment)

        try {
            val qrBitmap = com.example.neyza_insight.data.QrCodeHelper.createQR("pindahan:${entity.id}")
            imgQrCode.setImageBitmap(qrBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}