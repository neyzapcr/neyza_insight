package com.example.neyza_insight.Home.pertemuan_10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neyza_insight.databinding.ItemKelahiranBinding

class KelahiranAdapter(
    private val listKelahiran: List<KelahiranModel>
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

            // 🔥 GLIDE UNTUK MEMUNCULKAN GAMBAR KELAHIRAN
            Glide.with(holder.itemView.context)
                .load(data.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(imgDokumen)
        }
    }

    override fun getItemCount(): Int = listKelahiran.size
}