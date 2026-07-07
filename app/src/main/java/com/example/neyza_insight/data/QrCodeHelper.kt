package com.example.neyza_insight.data

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

object QrCodeHelper {
    fun createQR(text: String, width: Int = 500, height: Int = 500): Bitmap {
        val writer = QRCodeWriter()
        val matrix = writer.encode(
            text,
            BarcodeFormat.QR_CODE,
            width,
            height,
            mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
        )
        return Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).apply {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
        }
    }
}
