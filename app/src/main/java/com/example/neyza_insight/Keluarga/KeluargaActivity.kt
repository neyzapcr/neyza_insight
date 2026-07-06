package com.example.neyza_insight.Keluarga

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neyza_insight.data.AppDatabase
import com.example.neyza_insight.data.entity.KeluargaEntity
import com.example.neyza_insight.databinding.ActivityKeluargaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class KeluargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeluargaBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: KeluargaAdapter
    private val keluargaList = mutableListOf<KeluargaEntity>()

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            loadFamilies()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeluargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        // Toolbar setup
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // RecyclerView setup
        adapter = KeluargaAdapter(
            context = this,
            listKeluarga = keluargaList,
            onEditClick = { entity ->
                val intent = Intent(this, KeluargaFormActivity::class.java).apply {
                    putExtra("EXTRA_KELUARGA_ID", entity.id)
                }
                formLauncher.launch(intent)
            },
            onDeleteClick = { entity ->
                showDeleteConfirmationDialog(entity)
            },
            onItemClick = { entity ->
                showDetailDialog(entity)
            }
        )

        binding.rvKeluarga.layoutManager = LinearLayoutManager(this)
        binding.rvKeluarga.adapter = adapter

        // Search Bar setup
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // FAB setup
        binding.fabAddKeluarga.setOnClickListener {
            val intent = Intent(this, KeluargaFormActivity::class.java)
            formLauncher.launch(intent)
        }

        loadFamilies()
    }

    private fun loadFamilies() {
        lifecycleScope.launch {
            val data = db.keluargaDao().getAll()
            keluargaList.clear()
            keluargaList.addAll(data)
            adapter.updateData(keluargaList)
            updateCountLabel(data.size)
        }
    }

    private fun performSearch(query: String) {
        lifecycleScope.launch {
            val searchResults = if (query.isEmpty()) {
                db.keluargaDao().getAll()
            } else {
                db.keluargaDao().search("%$query%")
            }
            adapter.updateData(searchResults)
            updateCountLabel(searchResults.size)
        }
    }

    private fun updateCountLabel(count: Int) {
        binding.tvJumlahData.text = "$count data"
    }

    private fun showDeleteConfirmationDialog(entity: KeluargaEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Hapus Data Keluarga")
            .setMessage("Apakah Anda yakin ingin menghapus data kartu keluarga atas nama ${entity.namaKepalaKeluarga}?")
            .setPositiveButton("Ya") { dialog, _ ->
                lifecycleScope.launch {
                    db.keluargaDao().delete(entity)
                    Toast.makeText(this@KeluargaActivity, "Data Keluarga berhasil dihapus", Toast.LENGTH_SHORT).show()
                    loadFamilies()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDetailDialog(entity: KeluargaEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Detail Kartu Keluarga")
            .setMessage(
                "Nomor KK        : ${entity.noKk}\n" +
                "Kepala Keluarga : ${entity.namaKepalaKeluarga}\n" +
                "Alamat          : ${entity.alamat}\n" +
                "RT/RW           : ${entity.rtRw}\n" +
                "Dusun           : ${entity.dusun}\n" +
                "Jumlah Anggota  : ${entity.jumlahAnggota} orang"
            )
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
