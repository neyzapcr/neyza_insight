package com.example.neyza_insight.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

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

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, _ ->
                    sharedPref.edit().clear().apply()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}