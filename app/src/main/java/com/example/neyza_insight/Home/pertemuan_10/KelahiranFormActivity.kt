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
import com.example.neyza_insight.data.entity.KelahiranEntity
import com.example.neyza_insight.databinding.ActivityKelahiranFormBinding
import com.example.neyza_insight.reminder.ReminderDialogHelper
import com.example.neyza_insight.reminder.ReminderHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class KelahiranFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKelahiranFormBinding
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
        binding = ActivityKelahiranFormBinding.inflate(layoutInflater)
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
            supportActionBar?.title = "Edit Draft Kelahiran"
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

        // Save Draft Action
        binding.btnSaveDraft.setOnClickListener {
            val nama = binding.etNamaBayi.text.toString().trim()
            if (nama.isEmpty()) {
                binding.etNamaBayi.error = "Nama wajib diisi untuk menyimpan draft"
                return@setOnClickListener
            }

            val noAkta = binding.etNoAkta.text.toString().trim()
            val tanggalLahir = binding.etTanggalLahir.text.toString().trim()
            val tempatLahir = binding.etTempatLahir.text.toString().trim()
            val namaAyah = binding.etNamaAyah.text.toString().trim()
            val namaIbu = binding.etNamaIbu.text.toString().trim()
            var imageUrl = binding.etImageUrl.text.toString().trim()

            if (imageUrl.isEmpty()) {
                imageUrl = "https://randomuser.me/api/portraits/lego/1.jpg"
            }

            lifecycleScope.launch {
                val record = KelahiranEntity(
                    id = currentDraftId,
                    nama = nama,
                    noAkta = noAkta,
                    tanggalLahir = tanggalLahir,
                    tempatLahir = tempatLahir,
                    namaAyah = namaAyah,
                    namaIbu = namaIbu,
                    imageUrl = imageUrl,
                    status = "Draft"
                )
                val insertedId = db.kelahiranDao().insert(record)
                currentDraftId = insertedId.toInt()

                val resultIntent = android.content.Intent().apply {
                    putExtra("SHOW_SNACKBAR", true)
                    putExtra("DRAFT_ID", currentDraftId)
                    putExtra("EVENT_TYPE", "kelahiran")
                }
                setResult(android.app.Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        // Save Complete Action
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
                imageUrl = "https://randomuser.me/api/portraits/lego/1.jpg"
            }

            lifecycleScope.launch {
                val record = KelahiranEntity(
                    id = currentDraftId,
                    nama = nama,
                    noAkta = noAkta,
                    tanggalLahir = tanggalLahir,
                    tempatLahir = tempatLahir,
                    namaAyah = namaAyah,
                    namaIbu = namaIbu,
                    imageUrl = imageUrl,
                    status = "Selesai"
                )
                db.kelahiranDao().insert(record)
                Toast.makeText(this@KelahiranFormActivity, "Data Kelahiran berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun loadDraftData() {
        lifecycleScope.launch {
            val data = db.kelahiranDao().getAll()
            val draft = data.find { it.id == currentDraftId }
            draft?.let {
                binding.etNamaBayi.setText(it.nama)
                binding.etNoAkta.setText(it.noAkta)
                binding.etTanggalLahir.setText(it.tanggalLahir)
                binding.etTempatLahir.setText(it.tempatLahir)
                binding.etNamaAyah.setText(it.namaAyah)
                binding.etNamaIbu.setText(it.namaIbu)
                binding.etImageUrl.setText(it.imageUrl)
                if (it.imageUrl.isNotEmpty()) {
                    com.bumptech.glide.Glide.with(this@KelahiranFormActivity)
                        .load(it.imageUrl)
                        .placeholder(R.drawable.ic_document)
                        .error(R.drawable.ic_document)
                        .into(binding.imgPreview)
                }
            }
        }
    }
}
