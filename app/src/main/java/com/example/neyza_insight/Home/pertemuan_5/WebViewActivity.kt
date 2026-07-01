package com.example.neyza_insight.Home.pertemuan_5

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Web Data Penduduk"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.isNestedScrollingEnabled = true
        binding.webView.loadUrl("https://neyza-kependudukan.alwaysdata.net/")

        // Menggunakan listener scroll dari NestedScrollView untuk menyembunyikan/menampilkan Toolbar
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                binding.appBar.setExpanded(false, true)
            } else if (scrollY < oldScrollY) {
                binding.appBar.setExpanded(true, true)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_web_view, menu)

        // Konfigurasi SearchView untuk pencarian di WebView
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.queryHint = "Cari di halaman..."
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    binding.webView.findAllAsync(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    binding.webView.findAllAsync(newText)
                } else {
                    binding.webView.clearMatches()
                }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_settings -> {
                showSettingsDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSettingsDialog() {
        val options = arrayOf(
            "Refresh Halaman",
            "Aktifkan/Nonaktifkan JavaScript",
            "Bersihkan Cache",
            "Halaman Utama (Neyza Kependudukan)"
        )

        AlertDialog.Builder(this)
            .setTitle("Pengaturan WebView")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        binding.webView.reload()
                        Toast.makeText(this, "Halaman direfresh", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        val isEnabled = binding.webView.settings.javaScriptEnabled
                        binding.webView.settings.javaScriptEnabled = !isEnabled
                        val status = if (!isEnabled) "Aktif" else "Nonaktif"
                        Toast.makeText(this, "JavaScript $status", Toast.LENGTH_SHORT).show()
                        binding.webView.reload()
                    }
                    2 -> {
                        binding.webView.clearCache(true)
                        Toast.makeText(this, "Cache dibersihkan", Toast.LENGTH_SHORT).show()
                    }
                    3 -> {
                        binding.webView.loadUrl("https://neyza-kependudukan.alwaysdata.net/")
                        Toast.makeText(this, "Memuat halaman utama", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}