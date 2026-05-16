package com.example.neyza_insight.Selengkapnya

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.FragmentSelengkapnyaBinding

class SelengkapnyaFragment : Fragment(R.layout.fragment_selengkapnya) {

    private var _binding: FragmentSelengkapnyaBinding? = null
    private val binding get() = _binding!!

    private val menuList = listOf(
        MenuModel(
            iconRes   = R.drawable.ic_menu_about_app,
            judul     = "Tentang Aplikasi",
            deskripsi = "Informasi versi dan deskripsi aplikasi Bina Desa"
        ),
        MenuModel(
            iconRes   = R.drawable.ic_menu_desa,
            judul     = "Profil Desa",
            deskripsi = "Sejarah, visi, dan misi desa kami"
        ),
        MenuModel(
            iconRes   = R.drawable.ic_menu_kontak,
            judul     = "Kontak Desa",
            deskripsi = "Hubungi perangkat desa secara langsung"
        ),
        MenuModel(
            iconRes   = R.drawable.ic_menu_bantuan,
            judul     = "Bantuan",
            deskripsi = "Panduan penggunaan dan FAQ aplikasi"
        ),
        MenuModel(
            iconRes   = R.drawable.ic_menu_privacy,
            judul     = "Privacy Policy",
            deskripsi = "Kebijakan privasi dan keamanan data pengguna"
        ),
        MenuModel(
            iconRes   = R.drawable.ic_menu_kritik,
            judul     = "Kritik & Saran",
            deskripsi = "Sampaikan masukan untuk pengembangan aplikasi"
        ),
        MenuModel(
            iconRes   = R.drawable.ic_menu_versi,
            judul     = "Versi Aplikasi",
            deskripsi = "Bina Desa v1.0.0 — Build 2024"
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSelengkapnyaBinding.bind(view)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "More"
        }

        // Setup adapter
        val adapter = SelengkapnyaAdapter(requireContext(), menuList)
        binding.listViewMenu.adapter = adapter

        // Klik item → Toast
        binding.listViewMenu.setOnItemClickListener { _, _, position, _ ->
            val item = menuList[position]
            Toast.makeText(requireContext(), item.judul, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
