package com.example.neyza_insight.Selengkapnya

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.neyza_insight.databinding.ItemSelengkapnyaBinding

class SelengkapnyaAdapter(
    context: Context,
    private val menuList: List<MenuModel>
) : ArrayAdapter<MenuModel>(context, 0, menuList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSelengkapnyaBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        val item = menuList[position]

        binding.imgIcon.setImageResource(item.iconRes)
        binding.tvJudul.text = item.judul
        binding.tvDeskripsi.text = item.deskripsi

        return binding.root
    }
}
