package com.example.neyza_insight.Keluarga

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neyza_insight.data.entity.KeluargaEntity
import com.example.neyza_insight.databinding.ItemKeluargaBinding

class KeluargaAdapter(
    private val context: Context,
    private var listKeluarga: List<KeluargaEntity>,
    private val onEditClick: (KeluargaEntity) -> Unit,
    private val onDeleteClick: (KeluargaEntity) -> Unit,
    private val onItemClick: (KeluargaEntity) -> Unit
) : RecyclerView.Adapter<KeluargaAdapter.ViewHolder>() {

    private val avatarColors = listOf(
        "#3B82F6", "#1E40AF", "#2563EB", "#0EA5E9",
        "#6366F1", "#8B5CF6", "#EC4899", "#14B8A6"
    )

    inner class ViewHolder(val binding: ItemKeluargaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeluargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listKeluarga[position]
        with(holder.binding) {
            tvKepalaKeluarga.text = data.namaKepalaKeluarga
            tvNoKk.text = "No. KK: ${data.noKk}"
            tvAlamat.text = "📍 Alamat: ${data.alamat}"
            tvRtRwDusun.text = "RT/RW: ${data.rtRw}  •  Dusun: ${data.dusun}"
            tvJumlahAnggota.text = "👪 ${data.jumlahAnggota} Anggota"

            // Avatar initial and colored background
            tvAvatar.text = data.namaKepalaKeluarga.take(1).uppercase()
            tvAvatar.background?.setTint(
                Color.parseColor(avatarColors[position % avatarColors.size])
            )

            // Edit button click
            btnEdit.setOnClickListener {
                onEditClick(data)
            }

            // Delete button click
            btnDelete.setOnClickListener {
                onDeleteClick(data)
            }

            // Item click (detail)
            holder.itemView.setOnClickListener {
                onItemClick(data)
            }
        }
    }

    override fun getItemCount(): Int = listKeluarga.size

    fun updateData(newList: List<KeluargaEntity>) {
        listKeluarga = newList
        notifyDataSetChanged()
    }
}
