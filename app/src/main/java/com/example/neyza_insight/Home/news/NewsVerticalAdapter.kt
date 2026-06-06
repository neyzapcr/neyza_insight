package com.example.neyza_insight.Home.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neyza_insight.data.model.NewsItem
import com.example.neyza_insight.databinding.ItemNewsVerticalBinding

class NewsVerticalAdapter(private val items: List<NewsItem>) : RecyclerView.Adapter<NewsVerticalAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemNewsVerticalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvTitle.text = item.title

        // Menentukan Kategori Kependudukan secara dinamis berdasarkan judul berita
        val titleLower = item.title.lowercase()
        val categoryText = when {
            titleLower.contains("lahir") -> "Kelahiran"
            titleLower.contains("kematian") || titleLower.contains("meninggal") || titleLower.contains("wafat") -> "Kematian"
            titleLower.contains("pindah") || titleLower.contains("datang") || titleLower.contains("migrasi") -> "Perpindahan"
            titleLower.contains("keluarga") || titleLower.contains("kk") -> "Keluarga"
            else -> "Kependudukan"
        }
        holder.binding.tvCategory.text = categoryText

        // Format ISO Date (YYYY-MM-DDThh:mm:ss.sssZ) to YYYY-MM-DD
        val formattedDate = if (item.isoDate.length >= 10) {
            item.isoDate.substring(0, 10)
        } else {
            item.isoDate
        }
        holder.binding.tvDate.text = "$formattedDate • GNews"

        // Glide image loading dengan placeholders untuk CNN/GNews ImageUrl object
        val imageUrl = item.image?.large ?: item.image?.small
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_gallery)
            .into(holder.binding.imgNews)

        // Klik untuk membuka artikel berita di WebViewActivity di dalam aplikasi
        val openNewsListener = View.OnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, com.example.neyza_insight.Home.pertemuan_5.WebViewActivity::class.java).apply {
                putExtra("url", item.link)
                putExtra("title", item.title)
            }
            context.startActivity(intent)
        }
        holder.itemView.setOnClickListener(openNewsListener)
        holder.binding.tvAuthor.setOnClickListener(openNewsListener)
    }

    override fun getItemCount(): Int = items.size
}
