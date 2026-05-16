package com.example.neyza_insight.Home.pertemuan_9

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.DataWarga.WargaAdapter
import com.example.neyza_insight.DataWarga.WargaModel
import com.example.neyza_insight.databinding.ActivityDataWargaBinding
import com.google.android.material.chip.Chip

class DataWargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataWargaBinding

    private val wargaList = listOf(
        WargaModel("Ahmad Fauzi",     "1471010101800001", "Laki-laki",  "Islam",   "Petani",           "081234567890", "ahmad.fauzi@email.com",     "Jl. Merdeka No. 1, RT 01/RW 02"),
        WargaModel("Siti Rahayu",     "1471015505920002", "Perempuan",  "Islam",   "Guru",             "082345678901", "siti.rahayu@email.com",     "Jl. Pahlawan No. 5, RT 02/RW 01"),
        WargaModel("Budi Santoso",    "1471011203850003", "Laki-laki",  "Kristen", "Wiraswasta",       "083456789012", "budi.santoso@email.com",    "Jl. Sudirman No. 12, RT 03/RW 03"),
        WargaModel("Dewi Lestari",    "1471012007950004", "Perempuan",  "Islam",   "Bidan",            "084567890123", "dewi.lestari@email.com",    "Jl. Diponegoro No. 8, RT 01/RW 04"),
        WargaModel("Eko Prasetyo",    "1471010908780005", "Laki-laki",  "Islam",   "Nelayan",          "085678901234", "eko.prasetyo@email.com",    "Jl. Nelayan No. 3, RT 04/RW 02"),
        WargaModel("Fitri Handayani", "1471011506900006", "Perempuan",  "Islam",   "Perawat",          "086789012345", "fitri.handayani@email.com", "Jl. Kesehatan No. 7, RT 02/RW 03"),
        WargaModel("Gunawan Hadi",    "1471012211820007", "Laki-laki",  "Hindu",   "Pedagang",         "087890123456", "gunawan.hadi@email.com",    "Jl. Pasar No. 15, RT 05/RW 01"),
        WargaModel("Hana Pertiwi",    "1471010304980008", "Perempuan",  "Kristen", "Mahasiswa",        "088901234567", "hana.pertiwi@email.com",    "Jl. Pendidikan No. 2, RT 01/RW 01"),
        WargaModel("Irfan Maulana",   "1471011807750009", "Laki-laki",  "Islam",   "PNS",              "089012345678", "irfan.maulana@email.com",   "Jl. Pemerintahan No. 9, RT 03/RW 04"),
        WargaModel("Juwita Sari",     "1471012509880010", "Perempuan",  "Budha",   "Karyawan Swasta",  "081123456789", "juwita.sari@email.com",     "Jl. Industri No. 4, RT 02/RW 02")
    )

    private val filteredList = mutableListOf<WargaModel>()
    private lateinit var adapter: WargaAdapter
    private var filterJK = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataWargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar dengan tombol back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Inisialisasi list & adapter
        filteredList.addAll(wargaList)
        adapter = WargaAdapter(this, filteredList)
        binding.listViewWarga.adapter = adapter
        updateJumlahData()

        // Search real-time
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilter(s.toString(), filterJK)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Filter Chip jenis kelamin
        binding.chipGroupJK.setOnCheckedStateChangeListener { group, checkedIds ->
            val id = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            filterJK = group.findViewById<Chip>(id).text.toString()
            applyFilter(binding.etSearch.text.toString(), filterJK)
        }

        // Klik item → dialog detail
        binding.listViewWarga.setOnItemClickListener { _, _, position, _ ->
            showDetailDialog(filteredList[position])
        }
    }

    private fun applyFilter(query: String, jk: String) {
        filteredList.clear()
        filteredList.addAll(wargaList.filter { w ->
            val matchQuery = query.isEmpty() ||
                    w.nama.contains(query, ignoreCase = true) ||
                    w.nik.contains(query) ||
                    w.pekerjaan.contains(query, ignoreCase = true)
            val matchJK = jk == "Semua" || w.jenisKelamin == jk
            matchQuery && matchJK
        })
        adapter.notifyDataSetChanged()
        updateJumlahData()
    }

    private fun updateJumlahData() {
        binding.tvJumlahData.text = "${filteredList.size} data"
    }

    private fun showDetailDialog(w: WargaModel) {
        AlertDialog.Builder(this)
            .setTitle("Detail Warga")
            .setMessage(
                "Nama         : ${w.nama}\n" +
                        "NIK          : ${w.nik}\n" +
                        "Jenis Kel.   : ${w.jenisKelamin}\n" +
                        "Agama        : ${w.agama}\n" +
                        "Pekerjaan    : ${w.pekerjaan}\n" +
                        "No. Telp     : ${w.noTelp}\n" +
                        "Email        : ${w.email}\n" +
                        "Alamat       : ${w.alamat}"
            )
            .setPositiveButton("Tutup") { d, _ -> d.dismiss() }
            .show()
    }
}
