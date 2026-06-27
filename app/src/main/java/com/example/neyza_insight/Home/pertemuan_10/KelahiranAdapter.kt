package com.example.neyza_insight.Home.pertemuan_10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neyza_insight.data.entity.KelahiranEntity
import com.example.neyza_insight.databinding.ItemKelahiranBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class KelahiranAdapter(
    private val listKelahiran: List<KelahiranEntity>,
    private val onDeleteClick: (KelahiranEntity) -> Unit,
    private val onItemClick: (KelahiranEntity) -> Unit,
    private val onReminderClick: (KelahiranEntity) -> Unit
) : RecyclerView.Adapter<KelahiranAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemKelahiranBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKelahiranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listKelahiran[position]
        with(holder.binding) {
            tvNamaKelahiran.text = data.nama
            tvNoAkta.text = "No. Akta: ${data.noAkta}"
            tvTglLahir.text = "Tanggal Lahir: ${data.tanggalLahir}"
            tvTempatLahir.text = "Tempat Lahir: ${data.tempatLahir}"
            tvNamaAyah.text = "Nama Ayah: ${data.namaAyah}"
            tvNamaIbu.text = "Nama Ibu: ${data.namaIbu}"

            // Bind status
            tvStatus.text = data.status
            if (data.status == "Draft") {
                tvStatus.background?.setTint(android.graphics.Color.parseColor("#FF9800"))
            } else {
                tvStatus.background?.setTint(android.graphics.Color.parseColor("#4CAF50"))
            }

            // Click listener
            holder.itemView.setOnClickListener {
                onItemClick(data)
            }

            // Delete action
            btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Hapus Data Kelahiran")
                    .setMessage("Apakah Anda yakin ingin menghapus data kelahiran ini?")
                    .setPositiveButton("Ya") { dialog, _ ->
                        onDeleteClick(data)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            // Reminder action
            btnCardReminder.setOnClickListener {
                onReminderClick(data)
            }

            // Glide loader
            Glide.with(holder.itemView.context)
                .load(data.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(imgDokumen)
        }
    }

    override fun getItemCount(): Int = listKelahiran.size
}