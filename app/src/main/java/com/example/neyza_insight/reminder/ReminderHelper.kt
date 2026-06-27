package com.example.neyza_insight.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

object ReminderHelper {
    fun setReminder(context: Context, targetTab: Int, minutes: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("TARGET_TAB", targetTab)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            targetTab,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTimeMs = System.currentTimeMillis() + (minutes * 60 * 1000L)
        scheduleAlarm(alarmManager, triggerTimeMs, pendingIntent)
        Log.d("ReminderHelper", "Reminder diatur untuk tab: $targetTab dalam $minutes menit")
    }

    fun setReminderInSeconds(context: Context, targetTab: Int, seconds: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("TARGET_TAB", targetTab)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            targetTab,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTimeMs = System.currentTimeMillis() + (seconds * 1000L)
        scheduleAlarm(alarmManager, triggerTimeMs, pendingIntent)
        Log.d("ReminderHelper", "Reminder diatur untuk tab: $targetTab dalam $seconds detik")
    }

    fun setReminderAtTime(context: Context, targetTab: Int, triggerTimeMs: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("TARGET_TAB", targetTab)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            targetTab,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        scheduleAlarm(alarmManager, triggerTimeMs, pendingIntent)
        Log.d("ReminderHelper", "Reminder diatur untuk tab: $targetTab pada waktu milidetik: $triggerTimeMs")
    }

    // --- Draft Edit Flow Methods ---

    fun setDraftReminder(context: Context, eventType: String, draftId: Int, minutes: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("IS_DRAFT_EDIT_FLOW", true)
            putExtra("EVENT_TYPE", eventType)
            putExtra("DRAFT_ID", draftId)
        }

        val requestCode = eventType.hashCode() + draftId
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTimeMs = System.currentTimeMillis() + (minutes * 60 * 1000L)
        scheduleAlarm(alarmManager, triggerTimeMs, pendingIntent)
        Log.d("ReminderHelper", "Draft reminder diatur untuk $eventType ID $draftId dalam $minutes menit")
    }

    fun setDraftReminderInSeconds(context: Context, eventType: String, draftId: Int, seconds: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("IS_DRAFT_EDIT_FLOW", true)
            putExtra("EVENT_TYPE", eventType)
            putExtra("DRAFT_ID", draftId)
        }

        val requestCode = eventType.hashCode() + draftId
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTimeMs = System.currentTimeMillis() + (seconds * 1000L)
        scheduleAlarm(alarmManager, triggerTimeMs, pendingIntent)
        Log.d("ReminderHelper", "Draft reminder diatur untuk $eventType ID $draftId dalam $seconds detik")
    }

    fun setDraftReminderAtTime(context: Context, eventType: String, draftId: Int, triggerTimeMs: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("IS_DRAFT_EDIT_FLOW", true)
            putExtra("EVENT_TYPE", eventType)
            putExtra("DRAFT_ID", draftId)
        }

        val requestCode = eventType.hashCode() + draftId
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        scheduleAlarm(alarmManager, triggerTimeMs, pendingIntent)
        Log.d("ReminderHelper", "Draft reminder diatur untuk $eventType ID $draftId pada milidetik $triggerTimeMs")
    }

    private fun scheduleAlarm(alarmManager: AlarmManager, triggerTimeMs: Long, pendingIntent: PendingIntent) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTimeMs,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        triggerTimeMs,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMs,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMs,
                pendingIntent
            )
            Log.e("ReminderHelper", "SecurityException saat menjadwalkan alarm, fallback ke set biasa", e)
        }
    }
}

