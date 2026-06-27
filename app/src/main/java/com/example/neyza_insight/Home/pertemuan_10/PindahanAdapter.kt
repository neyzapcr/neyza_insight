package com.example.neyza_insight.Home.pertemuan_10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neyza_insight.data.entity.PindahanEntity
import com.example.neyza_insight.databinding.ItemPindahanBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PindahanAdapter(
    private val listPindahan: List<PindahanEntity>,
    private val onDeleteClick: (PindahanEntity) -> Unit,
    private val onItemClick: (PindahanEntity) -> Unit,
    private val onReminderClick: (PindahanEntity) -> Unit
) : RecyclerView.Adapter<PindahanAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPindahanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPindahanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPindahan[position]
        with(holder.binding) {
            tvNamaPindahan.text = "${item.nama} (${item.jenisKelamin})"
            tvNikPindahan.text = "NIK: ${item.nik}"
            tvAlamatAsal.text = "Asal: ${item.alamatAsal}"
            tvAlamatTujuan.text = "Tujuan: ${item.alamatTujuan}"
            tvAlasanPindah.text = "Alasan: ${item.alasanPindah}"
            tvTglPindah.text = "Tgl Pindah: ${item.tanggalPindah}"
            tvNoSuratPindah.text = "No. Surat: ${item.noSuratPindah}"

            // Bind status
            tvStatus.text = item.status
            if (item.status == "Draft") {
                tvStatus.background?.setTint(android.graphics.Color.parseColor("#FF9800"))
            } else {
                tvStatus.background?.setTint(android.graphics.Color.parseColor("#4CAF50"))
            }

            // Click listener
            holder.itemView.setOnClickListener {
                onItemClick(item)
            }

            // Delete action
            btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Hapus Data Perpindahan")
                    .setMessage("Apakah Anda yakin ingin menghapus data perpindahan ini?")
                    .setPositiveButton("Ya") { dialog, _ ->
                        onDeleteClick(item)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            // Reminder action
            btnCardReminder.setOnClickListener {
                onReminderClick(item)
            }

            // Glide loader
            Glide.with(holder.itemView.context)
                .load(item.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(imgDokumen)
        }
    }

    override fun getItemCount(): Int = listPindahan.size
}