package com.example.neyza_insight.Home.pertemuan_10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neyza_insight.databinding.ItemPindahanBinding

class PindahanAdapter(private val listPindahan: List<PindahanModel>) :
    RecyclerView.Adapter<PindahanAdapter.ViewHolder>() {

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

            // 🔥 GLIDE UNTUK MEMUNCULKAN GAMBAR PINDAHAN
            Glide.with(holder.itemView.context)
                .load(item.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(imgDokumen)
        }
    }

    override fun getItemCount(): Int = listPindahan.size
}