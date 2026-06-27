package com.example.neyza_insight.reminder

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.neyza_insight.databinding.DialogAturReminderBinding
import java.util.Calendar

object ReminderDialogHelper {

    fun showReminderDialog(context: Context, targetTab: Int, onDismissCallback: (() -> Unit)? = null) {
        // First check permissions
        if (!PermissionHelper.isNotificationPermissionGranted(context)) {
            if (context is android.app.Activity) {
                PermissionHelper.requestNotificationPermission(context)
            } else {
                Toast.makeText(context, "Izin notifikasi diperlukan untuk menyetel pengingat", Toast.LENGTH_SHORT).show()
            }
            return
        }

        showDurationSelector(context, targetTab, onDismissCallback)
    }

    private fun showDurationSelector(context: Context, targetTab: Int, onDismissCallback: (() -> Unit)?) {
        val inflater = LayoutInflater.from(context)
        val dialogBinding = DialogAturReminderBinding.inflate(inflater)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnOpt5Min.setOnClickListener {
            dialog.dismiss()
            showConfirmation(context, targetTab, "5 menit", 5, isSeconds = false, onDismissCallback)
        }

        dialogBinding.btnOpt10Min.setOnClickListener {
            dialog.dismiss()
            showConfirmation(context, targetTab, "10 menit", 10, isSeconds = false, onDismissCallback)
        }

        dialogBinding.btnOpt30Min.setOnClickListener {
            dialog.dismiss()
            showConfirmation(context, targetTab, "30 menit", 30, isSeconds = false, onDismissCallback)
        }

        dialogBinding.btnOpt10Sec.setOnClickListener {
            dialog.dismiss()
            showConfirmation(context, targetTab, "10 detik", 10, isSeconds = true, onDismissCallback)
        }

        dialogBinding.btnOptCustomMin.setOnClickListener {
            dialog.dismiss()
            showCustomMinutesDialog(context, targetTab, onDismissCallback)
        }

        dialogBinding.btnOptSpecificTime.setOnClickListener {
            dialog.dismiss()
            showSpecificTimeDialog(context, targetTab, onDismissCallback)
        }

        dialogBinding.btnCancelReminder.setOnClickListener {
            dialog.dismiss()
            onDismissCallback?.invoke()
        }

        dialog.setOnCancelListener {
            onDismissCallback?.invoke()
        }

        dialog.show()
    }

    private fun showConfirmation(
        context: Context,
        targetTab: Int,
        timeLabel: String,
        duration: Int,
        isSeconds: Boolean,
        onDismissCallback: (() -> Unit)?
    ) {
        AlertDialog.Builder(context)
            .setTitle("Simpan Reminder")
            .setMessage("Reminder akan muncul dalam $timeLabel. Simpan?")
            .setPositiveButton("Simpan") { _, _ ->
                if (isSeconds) {
                    ReminderHelper.setReminderInSeconds(context, targetTab, duration)
                } else {
                    ReminderHelper.setReminder(context, targetTab, duration)
                }
                Toast.makeText(context, "Reminder berhasil diatur untuk $timeLabel ke depan", Toast.LENGTH_SHORT).show()
                onDismissCallback?.invoke()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
                showDurationSelector(context, targetTab, onDismissCallback)
            }
            .setOnCancelListener {
                showDurationSelector(context, targetTab, onDismissCallback)
            }
            .show()
    }

    private fun showCustomMinutesDialog(context: Context, targetTab: Int, onDismissCallback: (() -> Unit)?) {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Contoh: 15"
        }

        val customDialog = AlertDialog.Builder(context)
            .setTitle("Menit Kustom")
            .setMessage("Masukkan jumlah menit untuk pengingat:")
            .setView(input)
            .setPositiveButton("Lanjut") { dialog, _ ->
                val text = input.text.toString().trim()
                val minutes = text.toIntOrNull()
                if (minutes != null && minutes > 0) {
                    dialog.dismiss()
                    showConfirmation(context, targetTab, "$minutes menit", minutes, isSeconds = false, onDismissCallback)
                } else {
                    Toast.makeText(context, "Masukkan jumlah menit yang valid", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    showCustomMinutesDialog(context, targetTab, onDismissCallback)
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
                showDurationSelector(context, targetTab, onDismissCallback)
            }
            .setOnCancelListener {
                showDurationSelector(context, targetTab, onDismissCallback)
            }
            .create()

        customDialog.show()
    }

    private fun showSpecificTimeDialog(context: Context, targetTab: Int, onDismissCallback: (() -> Unit)?) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val targetCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // If selected time is in the past, set it for the next day
                if (targetCalendar.timeInMillis <= System.currentTimeMillis()) {
                    targetCalendar.add(Calendar.DAY_OF_YEAR, 1)
                }

                val timeLabel = String.format("%02d:%02d", selectedHour, selectedMinute)
                val isTomorrow = targetCalendar.get(Calendar.DAY_OF_YEAR) != calendar.get(Calendar.DAY_OF_YEAR)
                val label = if (isTomorrow) "$timeLabel (Besok)" else timeLabel

                AlertDialog.Builder(context)
                    .setTitle("Simpan Reminder")
                    .setMessage("Reminder akan muncul pada pukul $label. Simpan?")
                    .setPositiveButton("Simpan") { _, _ ->
                        ReminderHelper.setReminderAtTime(context, targetTab, targetCalendar.timeInMillis)
                        Toast.makeText(context, "Reminder berhasil diatur untuk pukul $label", Toast.LENGTH_SHORT).show()
                        onDismissCallback?.invoke()
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                        showDurationSelector(context, targetTab, onDismissCallback)
                    }
                    .setOnCancelListener {
                        showDurationSelector(context, targetTab, onDismissCallback)
                    }
                    .show()
            },
            hour,
            minute,
            true
        )

        timePickerDialog.setOnCancelListener {
            showDurationSelector(context, targetTab, onDismissCallback)
        }

        timePickerDialog.show()
    }

    // --- Draft Edit Flow Dialogs ---

    fun showDraftReminderDialog(
        context: Context,
        eventType: String,
        draftId: Int,
        onDismissCallback: (() -> Unit)? = null
    ) {
        if (!PermissionHelper.isNotificationPermissionGranted(context)) {
            if (context is android.app.Activity) {
                PermissionHelper.requestNotificationPermission(context)
            } else {
                Toast.makeText(context, "Izin notifikasi diperlukan untuk menyetel pengingat", Toast.LENGTH_SHORT).show()
            }
            return
        }

        showDraftDurationSelector(context, eventType, draftId, onDismissCallback)
    }

    private fun showDraftDurationSelector(
        context: Context,
        eventType: String,
        draftId: Int,
        onDismissCallback: (() -> Unit)?
    ) {
        val inflater = LayoutInflater.from(context)
        val dialogBinding = DialogAturReminderBinding.inflate(inflater)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnOpt5Min.setOnClickListener {
            dialog.dismiss()
            showDraftConfirmation(context, eventType, draftId, "5 menit", 5, isSeconds = false, onDismissCallback)
        }

        dialogBinding.btnOpt10Min.setOnClickListener {
            dialog.dismiss()
            showDraftConfirmation(context, eventType, draftId, "10 menit", 10, isSeconds = false, onDismissCallback)
        }

        dialogBinding.btnOpt30Min.setOnClickListener {
            dialog.dismiss()
            showDraftConfirmation(context, eventType, draftId, "30 menit", 30, isSeconds = false, onDismissCallback)
        }

        dialogBinding.btnOpt10Sec.setOnClickListener {
            dialog.dismiss()
            showDraftConfirmation(context, eventType, draftId, "10 detik", 10, isSeconds = true, onDismissCallback)
        }

        dialogBinding.btnOptCustomMin.setOnClickListener {
            dialog.dismiss()
            showDraftCustomMinutesDialog(context, eventType, draftId, onDismissCallback)
        }

        dialogBinding.btnOptSpecificTime.setOnClickListener {
            dialog.dismiss()
            showDraftSpecificTimeDialog(context, eventType, draftId, onDismissCallback)
        }

        dialogBinding.btnCancelReminder.setOnClickListener {
            dialog.dismiss()
            onDismissCallback?.invoke()
        }

        dialog.setOnCancelListener {
            onDismissCallback?.invoke()
        }

        dialog.show()
    }

    private fun showDraftConfirmation(
        context: Context,
        eventType: String,
        draftId: Int,
        timeLabel: String,
        duration: Int,
        isSeconds: Boolean,
        onDismissCallback: (() -> Unit)?
    ) {
        AlertDialog.Builder(context)
            .setTitle("Simpan Reminder")
            .setMessage("Reminder akan muncul dalam $timeLabel. Simpan?")
            .setPositiveButton("Simpan") { _, _ ->
                if (isSeconds) {
                    ReminderHelper.setDraftReminderInSeconds(context, eventType, draftId, duration)
                } else {
                    ReminderHelper.setDraftReminder(context, eventType, draftId, duration)
                }
                Toast.makeText(context, "Reminder berhasil diatur untuk $timeLabel ke depan", Toast.LENGTH_SHORT).show()
                onDismissCallback?.invoke()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
                showDraftDurationSelector(context, eventType, draftId, onDismissCallback)
            }
            .setOnCancelListener {
                showDraftDurationSelector(context, eventType, draftId, onDismissCallback)
            }
            .show()
    }

    private fun showDraftCustomMinutesDialog(
        context: Context,
        eventType: String,
        draftId: Int,
        onDismissCallback: (() -> Unit)?
    ) {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Contoh: 15"
        }

        val customDialog = AlertDialog.Builder(context)
            .setTitle("Menit Kustom")
            .setMessage("Masukkan jumlah menit untuk pengingat:")
            .setView(input)
            .setPositiveButton("Lanjut") { dialog, _ ->
                val text = input.text.toString().trim()
                val minutes = text.toIntOrNull()
                if (minutes != null && minutes > 0) {
                    dialog.dismiss()
                    showDraftConfirmation(context, eventType, draftId, "$minutes menit", minutes, isSeconds = false, onDismissCallback)
                } else {
                    Toast.makeText(context, "Masukkan jumlah menit yang valid", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    showDraftCustomMinutesDialog(context, eventType, draftId, onDismissCallback)
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
                showDraftDurationSelector(context, eventType, draftId, onDismissCallback)
            }
            .setOnCancelListener {
                showDraftDurationSelector(context, eventType, draftId, onDismissCallback)
            }
            .create()

        customDialog.show()
    }

    private fun showDraftSpecificTimeDialog(
        context: Context,
        eventType: String,
        draftId: Int,
        onDismissCallback: (() -> Unit)?
    ) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val targetCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // If selected time is in the past, set it for the next day
                if (targetCalendar.timeInMillis <= System.currentTimeMillis()) {
                    targetCalendar.add(Calendar.DAY_OF_YEAR, 1)
                }

                val timeLabel = String.format("%02d:%02d", selectedHour, selectedMinute)
                val isTomorrow = targetCalendar.get(Calendar.DAY_OF_YEAR) != calendar.get(Calendar.DAY_OF_YEAR)
                val label = if (isTomorrow) "$timeLabel (Besok)" else timeLabel

                AlertDialog.Builder(context)
                    .setTitle("Simpan Reminder")
                    .setMessage("Reminder akan muncul pada pukul $label. Simpan?")
                    .setPositiveButton("Simpan") { _, _ ->
                        ReminderHelper.setDraftReminderAtTime(context, eventType, draftId, targetCalendar.timeInMillis)
                        Toast.makeText(context, "Reminder berhasil diatur untuk pukul $label", Toast.LENGTH_SHORT).show()
                        onDismissCallback?.invoke()
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                        showDraftDurationSelector(context, eventType, draftId, onDismissCallback)
                    }
                    .setOnCancelListener {
                        showDraftDurationSelector(context, eventType, draftId, onDismissCallback)
                    }
                    .show()
            },
            hour,
            minute,
            true
        )

        timePickerDialog.setOnCancelListener {
            showDraftDurationSelector(context, eventType, draftId, onDismissCallback)
        }

        timePickerDialog.show()
    }
}
