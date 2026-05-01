package com.example.neyza_insight.About

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about) {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAboutBinding.bind(view)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "About"
        }

        binding.aboutContainer.alpha = 0f
        binding.aboutContainer.translationY = 80f
        binding.aboutContainer.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .start()

        startValueAnimation()
        startFeatureAnimation()
    }

    private fun startValueAnimation() {
        val values = listOf(
            binding.valueFast,
            binding.valueAccurate,
            binding.valueTransparent
        )

        values.forEach {
            it.alpha = 0f
            it.translationY = 40f
            it.scaleX = 0.85f
            it.scaleY = 0.85f
        }

        values.forEachIndexed { index, item ->
            item.animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay((index * 150).toLong())
                .setDuration(500)
                .withEndAction {
                    startFloatingAnimation(item)
                }
                .start()
        }
    }

    private fun startFeatureAnimation() {
        val features = listOf(
            binding.feature1,
            binding.feature2,
            binding.feature3,
            binding.feature4
        )

        features.forEach {
            it.alpha = 0f
            it.translationY = 40f
            it.scaleX = 0.85f
            it.scaleY = 0.85f
        }

        features.forEachIndexed { index, viewItem ->
            viewItem.animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay((index * 120).toLong())
                .setDuration(400)
                .start()
        }
    }

    private fun startFloatingAnimation(view: View) {
        view.animate()
            .translationY(-8f)
            .setDuration(800)
            .withEndAction {
                view.animate()
                    .translationY(0f)
                    .setDuration(800)
                    .withEndAction {
                        startFloatingAnimation(view)
                    }
                    .start()
            }
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}