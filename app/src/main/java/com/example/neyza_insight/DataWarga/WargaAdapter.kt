package com.example.neyza_insight.DataWarga

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.neyza_insight.databinding.ItemWargaBinding
import com.google.android.material.snackbar.Snackbar

class WargaAdapter(
    context: Context,
    private val wargaList: List<WargaModel>
) : ArrayAdapter<WargaModel>(context, 0, wargaList) {

    private val avatarColors = listOf(
        "#3B82F6", "#1E40AF", "#2563EB", "#0EA5E9",
        "#6366F1", "#8B5CF6", "#EC4899", "#14B8A6"
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemWargaBinding.inflate(LayoutInflater.from(context), parent, false)
        val data = wargaList[position]

        // Avatar — inisial + warna bergantian
        binding.tvAvatar.text = data.nama.take(1).uppercase()
        binding.tvAvatar.background.setTint(
            Color.parseColor(avatarColors[position % avatarColors.size])
        )

        // Data utama
        binding.tvNama.text = data.nama
        binding.tvNik.text = "NIK: ${data.nik}"

        // Badge jenis kelamin
        binding.tvBadgeJK.text = if (data.jenisKelamin == "Laki-laki") "♂ L" else "♀ P"

        // Info baris bawah
        binding.tvAgama.text = "🕌 ${data.agama}"
        binding.tvPekerjaan.text = "💼 ${data.pekerjaan}"
        binding.tvNoTelp.text = "📞 ${data.noTelp}  •  ✉ ${data.email}"
        binding.tvAlamat.text = "📍 ${data.alamat}"

        // Snackbar saat item diklik
        binding.root.setOnClickListener {
            Snackbar.make(
                parent,
                "${data.nama} — ${data.pekerjaan} | ${data.noTelp}",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }
}
