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
import com.example.neyza_insight.data.entity.KematianEntity
import com.example.neyza_insight.databinding.ActivityKematianFormBinding
import com.example.neyza_insight.reminder.ReminderDialogHelper
import com.example.neyza_insight.reminder.ReminderHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class KematianFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKematianFormBinding
    private lateinit var db: AppDatabase
    private var currentDraftId: Int = 0

    private var currentPhotoUri: android.net.Uri? = null

    private val cameraLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            currentPhotoUri?.let { uri ->
                binding.etImageUrl.setText(uri.toString())
                com.bumptech.glide.Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_document)
                    .into(binding.imgPreview)
                // Refresh galeri
                sendBroadcast(android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            android.widget.Toast.makeText(this, "Izin kamera diperlukan", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val localFile = saveUriToLocalFile(it)
            localFile?.let { file ->
                val localUri = android.net.Uri.fromFile(file)
                binding.etImageUrl.setText(localUri.toString())
                com.bumptech.glide.Glide.with(this)
                    .load(localUri)
                    .placeholder(R.drawable.ic_document)
                    .into(binding.imgPreview)
            }
        }
    }

    private fun saveUriToLocalFile(uri: android.net.Uri): java.io.File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = java.io.File(filesDir, "doc_${System.currentTimeMillis()}.jpg")
            val outputStream = java.io.FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun hasCameraPermission(): Boolean {
        return androidx.core.content.ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val intent = android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        currentPhotoUri = createGalleryPhotoUri()
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, currentPhotoUri)
        cameraLauncher.launch(intent)
    }

    private fun createGalleryPhotoUri(): android.net.Uri {
        val folderName = "TestCaptures"
        val values = android.content.ContentValues().apply {
            put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${folderName}")
            }
        }
        return contentResolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw RuntimeException("Gagal membuat URI MediaStore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKematianFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        // Set up Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Camera & Gallery click listeners
        binding.btnCamera.setOnClickListener {
            if (hasCameraPermission()) {
                openCamera()
            } else {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }

        binding.btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        // Detect Edit Mode
        val draftId = intent.getIntExtra("EXTRA_DRAFT_ID", 0)
        if (draftId > 0) {
            currentDraftId = draftId
            supportActionBar?.title = "Edit Draft Kematian"
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

        // Save Draft Action
        binding.btnSaveDraft.setOnClickListener {
            val nama = binding.etNamaKematian.text.toString().trim()
            if (nama.isEmpty()) {
                binding.etNamaKematian.error = "Nama wajib diisi untuk menyimpan draft"
                return@setOnClickListener
            }

            val nik = binding.etNik.text.toString().trim()
            val noSurat = binding.etNoSurat.text.toString().trim()
            val tanggalMeninggal = binding.etTanggalMeninggal.text.toString().trim()
            val lokasi = binding.etLokasi.text.toString().trim()
            val sebab = binding.etSebab.text.toString().trim()
            val alamat = binding.etAlamat.text.toString().trim()
            var imageUrl = binding.etImageUrl.text.toString().trim()

            val jenisKelamin = if (binding.rbLaki.isChecked) "Laki-laki" else "Perempuan"

            if (imageUrl.isEmpty()) {
                imageUrl = "https://randomuser.me/api/portraits/lego/2.jpg"
            }

            lifecycleScope.launch {
                val record = KematianEntity(
                    id = currentDraftId,
                    nama = nama,
                    nik = nik,
                    noSurat = noSurat,
                    tanggalMeninggal = tanggalMeninggal,
                    lokasi = lokasi,
                    sebabKematian = sebab,
                    jenisKelamin = jenisKelamin,
                    alamat = alamat,
                    imageUrl = imageUrl,
                    status = "Draft"
                )
                val insertedId = db.kematianDao().insert(record)
                currentDraftId = insertedId.toInt()

                val resultIntent = android.content.Intent().apply {
                    putExtra("SHOW_SNACKBAR", true)
                    putExtra("DRAFT_ID", currentDraftId)
                    putExtra("EVENT_TYPE", "kematian")
                }
                setResult(android.app.Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        // Save Complete Action
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
                imageUrl = "https://randomuser.me/api/portraits/lego/2.jpg"
            }

            lifecycleScope.launch {
                val record = KematianEntity(
                    id = currentDraftId,
                    nama = nama,
                    nik = nik,
                    noSurat = noSurat,
                    tanggalMeninggal = tanggalMeninggal,
                    lokasi = lokasi,
                    sebabKematian = sebab,
                    jenisKelamin = jenisKelamin,
                    alamat = alamat,
                    imageUrl = imageUrl,
                    status = "Selesai"
                )
                db.kematianDao().insert(record)
                Toast.makeText(this@KematianFormActivity, "Data Kematian berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun loadDraftData() {
        lifecycleScope.launch {
            val data = db.kematianDao().getAll()
            val draft = data.find { it.id == currentDraftId }
            draft?.let {
                binding.etNamaKematian.setText(it.nama)
                binding.etNik.setText(it.nik)
                binding.etNoSurat.setText(it.noSurat)
                binding.etTanggalMeninggal.setText(it.tanggalMeninggal)
                binding.etLokasi.setText(it.lokasi)
                binding.etSebab.setText(it.sebabKematian)
                binding.etAlamat.setText(it.alamat)
                binding.etImageUrl.setText(it.imageUrl)
                if (it.imageUrl.isNotEmpty()) {
                    com.bumptech.glide.Glide.with(this@KematianFormActivity)
                        .load(it.imageUrl)
                        .placeholder(R.drawable.ic_document)
                        .error(R.drawable.ic_document)
                        .into(binding.imgPreview)
                }
                if (it.jenisKelamin == "Laki-laki") {
                    binding.rbLaki.isChecked = true
                } else {
                    binding.rbPerempuan.isChecked = true
                }
            }
        }
    }
}
