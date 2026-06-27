package com.example.neyza_insight.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val isDraftEditFlow = intent.getBooleanExtra("IS_DRAFT_EDIT_FLOW", false)
        if (isDraftEditFlow) {
            val draftId = intent.getIntExtra("DRAFT_ID", 0)
            val eventType = intent.getStringExtra("EVENT_TYPE") ?: ""
            Log.d("ReminderReceiver", "Alarm dipicu untuk edit draft: $eventType, id: $draftId")
            NotificationHelper.showDraftEditNotification(context, eventType, draftId)
        } else {
            val targetTab = intent.getIntExtra("TARGET_TAB", 0)
            Log.d("ReminderReceiver", "Alarm dipicu untuk tab: $targetTab")
            NotificationHelper.showNotification(context, targetTab)
        }
    }
}
