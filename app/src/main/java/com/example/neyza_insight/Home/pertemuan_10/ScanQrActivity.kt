package com.example.neyza_insight.Home.pertemuan_10

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.neyza_insight.databinding.ActivityScanQrBinding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanQrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrBinding
    private lateinit var cameraExecutor: ExecutorService
    private var isProcessed = false

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Izin kamera diperlukan untuk melakukan pemindaian", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (hasCameraPermission()) {
            startCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .apply {
                    setAnalyzer(cameraExecutor) { imageProxy ->
                        if (isProcessed) {
                            imageProxy.close()
                            return@setAnalyzer
                        }
                        val mediaImage = imageProxy.image ?: return@setAnalyzer imageProxy.close()
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                if (barcodes.isNotEmpty() && !isProcessed) {
                                    val rawValue = barcodes[0].rawValue
                                    if (rawValue != null && isValidQrCode(rawValue)) {
                                        isProcessed = true
                                        runOnUiThread {
                                            handleDetectedQr(rawValue)
                                        }
                                    }
                                }
                            }
                            .addOnCompleteListener { imageProxy.close() }
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                Log.e("ScanQrActivity", "Gagal mulai kamera", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun handleDetectedQr(rawValue: String) {
        val parts = rawValue.split(":")
        val type = parts[0]
        val id = parts[1].toInt()

        lifecycleScope.launch(Dispatchers.Main) {
            binding.tvScanResult.text = "Memproses data..."

            val db = com.example.neyza_insight.data.AppDatabase.getInstance(this@ScanQrActivity)
            val name = withContext(Dispatchers.IO) {
                when (type) {
                    "kelahiran" -> db.kelahiranDao().getById(id)?.nama
                    "kematian" -> db.kematianDao().getById(id)?.nama
                    "pindahan" -> db.pindahanDao().getById(id)?.nama
                    else -> null
                }
            }

            if (name != null) {
                val formattedType = when (type) {
                    "kelahiran" -> "Kelahiran"
                    "kematian" -> "Kematian"
                    "pindahan" -> "Perpindahan"
                    else -> type
                }
                binding.tvScanResult.text = "Hasil: Data $formattedType Ditemukan!\nNama: $name"
                delay(2000)

                val resultIntent = Intent().apply {
                    putExtra("EXTRA_SCAN_RESULT", rawValue)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                binding.tvScanResult.text = "Data $type dengan ID $id tidak ditemukan"
                delay(2000)
                isProcessed = false
                binding.tvScanResult.text = "Arahkan ke QR Code"
            }
        }
    }

    private fun isValidQrCode(value: String): Boolean {
        val parts = value.split(":")
        if (parts.size != 2) return false
        val type = parts[0]
        val id = parts[1].toIntOrNull()
        return (type == "kelahiran" || type == "kematian" || type == "pindahan") && id != null
    }

    override fun onDestroy() {
        super.onDestroy()
        scanner.close()
        cameraExecutor.shutdown()
    }
}
