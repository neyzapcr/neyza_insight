package com.example.neyza_insight.Home.pertemuan_10

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DataPeristiwaAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // Jumlah total tab peristiwa yang ada (Kelahiran, Kematian, Perpindahan)
    override fun getItemCount(): Int = 3

    // Menentukan Fragment mana yang akan ditampilkan berdasarkan posisi tab
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabKelahiranFragment()
            1 -> TabKematianFragment()
            2 -> TabPindahanFragment()
            else -> throw IllegalStateException("Posisi tidak valid")
        }
    }
}