package com.example.neyza_insight.Profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProfileBinding.bind(view)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Profile"
        }

        animateProfile()

        binding.btnGithub.setOnClickListener {
            openLink("https://github.com/neyzapcr")
        }

        binding.btnInstagram.setOnClickListener {
            openLink("https://instagram.com/neza-sha")
        }

        binding.btnLinkedin.setOnClickListener {
            openLink("linkedin.com/in/neyza-shafalika-s0020244636")
        }

        binding.btnWhatsapp.setOnClickListener {
            openLink("https://wa.me/6289652437006")
        }

        binding.btnEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:neyza24si@mahasiswa.pcr.ac.id")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Halo dari aplikasi Neyza Insight")
            startActivity(intent)
        }
    }

    private fun animateProfile() {
        val views = listOf(
            binding.imgProfile,
            binding.tvName,
            binding.tvNim,
            binding.tvMajor,
            binding.tvCampus,
            binding.contactMenu,
            binding.tvAboutMe,
            binding.tvDescription,
        )

        views.forEach {
            it.alpha = 0f
            it.translationY = 45f
        }

        views.forEachIndexed { index, item ->
            item.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay((index * 120).toLong())
                .setDuration(450)
                .start()
        }
    }

    private fun openLink(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}