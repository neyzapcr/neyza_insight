package com.example.neyza_insight.Home.pertemuan_10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neyza_insight.data.entity.KematianEntity
import com.example.neyza_insight.databinding.ItemKematianBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class KematianAdapter(
    private val listKematian: List<KematianEntity>,
    private val onDeleteClick: (KematianEntity) -> Unit
) : RecyclerView.Adapter<KematianAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemKematianBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKematianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listKematian[position]
        with(holder.binding) {
            tvNamaKematian.text = "${item.nama} (${item.jenisKelamin})"
            tvNikKematian.text = "NIK: ${item.nik}"
            tvTglMeninggal.text = "Wafat: ${item.tanggalMeninggal}"
            tvLokasiMeninggal.text = "Tempat: ${item.lokasi}"
            tvSebabKematian.text = "Sebab: ${item.sebabKematian}"
            tvNoSuratKematian.text = "No. Surat: ${item.noSurat}"
            tvAlamatKematian.text = "Alamat: ${item.alamat}"

            // Delete action
            btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Hapus Data Kematian")
                    .setMessage("Apakah Anda yakin ingin menghapus data kematian ini?")
                    .setPositiveButton("Ya") { dialog, _ ->
                        onDeleteClick(item)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            // Glide loader
            Glide.with(holder.itemView.context)
                .load(item.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(imgDokumen)
        }
    }

    override fun getItemCount(): Int = listKematian.size
}