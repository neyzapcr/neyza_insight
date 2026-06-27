package com.example.neyza_insight.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.neyza_insight.Home.pertemuan_10.DataPeristiwaActivity
import com.example.neyza_insight.Home.pertemuan_10.KelahiranFormActivity
import com.example.neyza_insight.Home.pertemuan_10.KematianFormActivity
import com.example.neyza_insight.Home.pertemuan_10.PindahanFormActivity
import com.example.neyza_insight.R

object NotificationHelper {
    private const val CHANNEL_ID = "reminder_channel"
    private const val CHANNEL_NAME = "Draft Data Reminder"
    private const val CHANNEL_DESC = "Notifikasi untuk mengingatkan pengisian draft data peristiwa"
    private const val NOTIFICATION_ID = 1001

    fun showNotification(context: Context, targetTab: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Setup Intent
        val intent = Intent(context, DataPeristiwaActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("TARGET_TAB", targetTab)
            putExtra("FILTER_DRAFT", true)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            targetTab, // Unique request code per tab to prevent overriding
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_document)
            .setContentTitle("Draft Data Belum Diselesaikan")
            .setContentText("Anda masih memiliki data peristiwa yang berstatus Draft. Ketuk notifikasi untuk melanjutkan pengisian data.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID + targetTab, builder.build())
    }

    fun showDraftEditNotification(context: Context, eventType: String, draftId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Determine target Activity based on event type
        val activityClass = when (eventType) {
            "kelahiran" -> KelahiranFormActivity::class.java
            "kematian" -> KematianFormActivity::class.java
            "pindahan" -> PindahanFormActivity::class.java
            else -> DataPeristiwaActivity::class.java
        }

        // Setup Intent to open the form in edit mode
        val intent = Intent(context, activityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_DRAFT_ID", draftId)
        }

        // Unique request code based on eventType and draftId
        val requestCode = eventType.hashCode() + draftId

        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification with specified text
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_document)
            .setContentTitle("Draft Data Belum Lengkap")
            .setContentText("Data peristiwa belum lengkap, tap untuk melengkapi")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Post notification with unique ID
        notificationManager.notify(requestCode, builder.build())
    }
}
