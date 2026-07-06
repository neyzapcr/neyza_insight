package com.example.neyza_insight.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.FragmentHomeBinding
import com.example.neyza_insight.Home.pertemuan_2.MainActivity
import com.example.neyza_insight.Home.pertemuan_3.LoginActivity
import com.example.neyza_insight.Home.pertemuan_4.CategoryActivity
import com.example.neyza_insight.Home.pertemuan_4.FavoriteActivity
import com.example.neyza_insight.Home.pertemuan_5.WebViewActivity
import com.example.neyza_insight.Home.pertemuan_9.DataWargaActivity
import com.example.neyza_insight.Home.pertemuan_10.DataPeristiwaActivity
import com.example.neyza_insight.Keluarga.KeluargaActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neyza_insight.data.api.NewsApiClient
import com.example.neyza_insight.Home.news.NewsAdapter
import com.example.neyza_insight.Home.news.NewsVerticalAdapter
import com.example.neyza_insight.data.model.NewsItem
import com.example.neyza_insight.data.model.ImageUrl
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Home"
        }

        startHomeAnimation()
        loadNews()

        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "User") ?: "User"

        val pageTitle = "Welcome $username!"
        binding.tvWelcome.text = pageTitle

        binding.btnRumusRuang.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("page_title", pageTitle)
            startActivity(intent)
        }

        binding.btnCategory.setOnClickListener {
            val intent = Intent(requireContext(), CategoryActivity::class.java)
            intent.putExtra("page_title", pageTitle)
            intent.putExtra("name", "Neyza")
            intent.putExtra("from", "Pekanbaru")
            intent.putExtra("age", 20)
            startActivity(intent)
        }

        binding.btnFavorite.setOnClickListener {
            val intent = Intent(requireContext(), FavoriteActivity::class.java)
            intent.putExtra("page_title", pageTitle)
            intent.putExtra("name", "Neyza")
            intent.putExtra("from", "Pekanbaru")
            intent.putExtra("age", 20)
            startActivity(intent)
        }

        binding.btnWebView.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            startActivity(intent)
        }

        binding.btnDataWarga.setOnClickListener {
            val intent = Intent(requireContext(), DataWargaActivity::class.java)
            startActivity(intent)
        }

        binding.btnDataKeluarga.setOnClickListener {
            val intent = Intent(requireContext(), KeluargaActivity::class.java)
            startActivity(intent)
        }

        binding.btnDataPeristiwa.setOnClickListener {
            val intent = Intent(requireContext(), DataPeristiwaActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, _ ->
                    // Hanya hapus status login, username & password tetap tersimpan
                    // agar user bisa login lagi tanpa perlu register ulang
                    sharedPref.edit().putBoolean("isLogin", false).apply()
                    dialog.dismiss()

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                    Snackbar.make(binding.root, "Logout dibatalkan", Snackbar.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    private fun startHomeAnimation() {
        val views = listOf(
            binding.imgLogoTop,
            binding.btnLogout,
            binding.tvWelcome,
            binding.cardHighlight,
            binding.statContainer,
            binding.cardMenu
        )

        views.forEach {
            it.alpha = 0f
            it.translationY = 45f
            it.scaleX = 0.95f
            it.scaleY = 0.95f
        }

        views.forEachIndexed { index, item ->
            item.animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay((index * 110).toLong())
                .setDuration(450)
                .start()
        }
    }

    private fun loadNews() {
        lifecycleScope.launch {
            try {
                val response1 = NewsApiClient.apiService.getBeritaKependudukan(
                    query = "kependudukan indonesia"
                )
                val response2 = NewsApiClient.apiService.getBeritaKependudukan(
                    query = "sensus penduduk indonesia"
                )
                val response3 = NewsApiClient.apiService.getBeritaKependudukan(
                    query = "dukcapil kelahiran kematian penduduk"
                )
                val response4 = NewsApiClient.apiService.getBeritaKependudukan(
                    query = "pertumbuhan penduduk indonesia"
                )

                val allNews = (response1.data + response2.data + response3.data + response4.data)
                    .distinctBy { it.url }
                    .filter { article ->
                        val title = article.title?.lowercase() ?: ""
                        val desc = article.description?.lowercase() ?: ""

                        // blacklist — buang artikel yang mengandung kata ini
                        val blacklist = listOf(
                            "seks", "sex", "film", "sinopsis", "anime", "artis",
                            "meninggal dunia", "celebrity", "hiburan", "musik"
                        )
                        val lolosBlacklist = blacklist.none { title.contains(it) || desc.contains(it) }

                        // whitelist — harus ada salah satu keyword ini
                        val keywords = listOf(
                            "penduduk", "kependudukan", "kelahiran", "kematian",
                            "dukcapil", "sensus", "migrasi", "demografi",
                            "kepadatan", "kartu keluarga", "administrasi"
                        )
                        val lolosKeyword = keywords.any { title.contains(it) || desc.contains(it) }

                        lolosBlacklist && lolosKeyword
                    }
                    .map { article ->
                        NewsItem(
                            title = article.title ?: "",
                            link = article.url ?: "",
                            isoDate = article.published_at ?: "",
                            image = ImageUrl(small = article.image_url, large = article.image_url),
                            description = article.description ?: ""
                        )
                    }

                Log.d("NEWS_DEBUG", "Total setelah filter: ${allNews.size}")

                val topNews = allNews.take(3)
                val otherNews = allNews.drop(3).take(15)

                binding.rvNews.adapter = NewsAdapter(topNews)
                binding.rvNews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                binding.rvNewsVertical.adapter = NewsVerticalAdapter(otherNews)
                binding.rvNewsVertical.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            } catch (e: Exception) {
                Log.e("NEWS_DEBUG", "Error: ${e.message}")
                Toast.makeText(requireContext(), "Gagal memuat berita", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}