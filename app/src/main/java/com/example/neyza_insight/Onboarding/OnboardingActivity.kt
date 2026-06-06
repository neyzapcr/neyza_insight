package com.example.neyza_insight.Onboarding

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.neyza_insight.Home.pertemuan_3.LoginActivity
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private var nextButtonWidthAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Play premium entrance animations
        playEntranceAnimation()

        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // Setup ViewPager dengan adapter
        val fragmentsList = listOf(Onboarding1Fragment(), Onboarding2Fragment(), Onboarding3Fragment())
        val adapter = OnboardingFragmentAdapter(this, fragmentsList)
        binding.viewPagerOnboarding.adapter = adapter

        // Atur animasi PageTransformer (parallax & scaling)
        binding.viewPagerOnboarding.setPageTransformer(OnboardingPageTransformer())

        // Hubungkan DotsIndicator dengan ViewPager2
        binding.dotIndicator.attachTo(binding.viewPagerOnboarding)

        // Setup page change callback untuk mengubah text/visibility tombol dengan animasi
        binding.viewPagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // Back button visibility with smooth fade animation
                if (position == 0) {
                    if (binding.btnBack.visibility == View.VISIBLE) {
                        binding.btnBack.animate()
                            .alpha(0f)
                            .setDuration(250)
                            .withEndAction { binding.btnBack.visibility = View.GONE }
                            .start()
                    }
                } else {
                    if (binding.btnBack.visibility != View.VISIBLE) {
                        binding.btnBack.visibility = View.VISIBLE
                        binding.btnBack.alpha = 0f
                        binding.btnBack.animate()
                            .alpha(1f)
                            .setDuration(250)
                            .start()
                    }
                }

                // Skip button visibility with smooth fade animation
                if (position == fragmentsList.size - 1) {
                    if (binding.btnSkip.visibility == View.VISIBLE) {
                        binding.btnSkip.animate()
                            .alpha(0f)
                            .translationX(40f)
                            .setDuration(250)
                            .withEndAction { binding.btnSkip.visibility = View.GONE }
                            .start()
                    }
                } else {
                    if (binding.btnSkip.visibility != View.VISIBLE) {
                        binding.btnSkip.visibility = View.VISIBLE
                        binding.btnSkip.alpha = 0f
                        binding.btnSkip.translationX = 40f
                        binding.btnSkip.animate()
                            .alpha(1f)
                            .translationX(0f)
                            .setDuration(250)
                            .start()
                    }
                }

                // Next Button layout morphing (Circle Arrow vs. Pill "Ayo Mulai")
                animateNextButton(position == fragmentsList.size - 1)
            }
        })

        // Listener tombol Skip (Lewati)
        binding.btnSkip.setOnClickListener {
            completeOnboarding()
        }

        // Listener tombol Kembali (Back)
        binding.btnBack.setOnClickListener {
            val currentItem = binding.viewPagerOnboarding.currentItem
            if (currentItem > 0) {
                binding.viewPagerOnboarding.currentItem = currentItem - 1
            }
        }

        // Listener tombol Lanjut / Ayo Mulai
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPagerOnboarding.currentItem
            if (currentItem < fragmentsList.size - 1) {
                binding.viewPagerOnboarding.currentItem = currentItem + 1
            } else {
                completeOnboarding()
            }
        }
    }

    private fun playEntranceAnimation() {
        // Initial state
        binding.logoHorizontal.alpha = 0f
        binding.logoHorizontal.translationY = -40f
        binding.btnSkip.alpha = 0f
        binding.btnSkip.translationY = -40f
        binding.viewPagerOnboarding.alpha = 0f
        binding.viewPagerOnboarding.translationY = 80f
        binding.navigationContainer.alpha = 0f
        binding.navigationContainer.translationY = 80f

        // Play animations
        binding.logoHorizontal.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(700)
            .setInterpolator(DecelerateInterpolator())
            .start()

        binding.btnSkip.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(700)
            .setInterpolator(DecelerateInterpolator())
            .start()

        binding.viewPagerOnboarding.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(900)
            .setStartDelay(150)
            .setInterpolator(DecelerateInterpolator())
            .start()

        binding.navigationContainer.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(900)
            .setStartDelay(300)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun animateNextButton(toPill: Boolean) {
        nextButtonWidthAnimator?.cancel()
        val density = resources.displayMetrics.density
        val startWidth = binding.btnNext.width
        val targetWidthDp = if (toPill) 110 else 44
        val endWidth = (targetWidthDp * density).toInt()

        if (startWidth == 0) {
            val params = binding.btnNext.layoutParams
            params.width = endWidth
            binding.btnNext.layoutParams = params
            if (toPill) {
                binding.btnNext.icon = null
                binding.btnNext.text = "Ayo Mulai"
                val paddingHorizontal = (12 * density).toInt()
                binding.btnNext.setPadding(paddingHorizontal, 0, paddingHorizontal, 0)
            } else {
                binding.btnNext.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_forward)
                binding.btnNext.text = ""
                binding.btnNext.setPadding(0, 0, 0, 0)
            }
            return
        }

        if (startWidth == endWidth) return

        if (toPill) {
            binding.btnNext.alpha = 1f
            binding.btnNext.icon = null
            binding.btnNext.text = ""

            nextButtonWidthAnimator = ValueAnimator.ofInt(startWidth, endWidth).apply {
                duration = 250
                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    val params = binding.btnNext.layoutParams
                    params.width = value
                    binding.btnNext.layoutParams = params
                }
                addListener(object : android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        binding.btnNext.text = "Ayo Mulai"
                        val paddingHorizontal = (12 * density).toInt()
                        binding.btnNext.setPadding(paddingHorizontal, 0, paddingHorizontal, 0)

                        binding.btnNext.alpha = 0f
                        binding.btnNext.animate().alpha(1f).setDuration(150).start()
                    }
                })
                start()
            }
        } else {
            binding.btnNext.alpha = 1f
            binding.btnNext.text = ""
            binding.btnNext.setPadding(0, 0, 0, 0)

            nextButtonWidthAnimator = ValueAnimator.ofInt(startWidth, endWidth).apply {
                duration = 250
                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    val params = binding.btnNext.layoutParams
                    params.width = value
                    binding.btnNext.layoutParams = params
                }
                addListener(object : android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        binding.btnNext.icon = ContextCompat.getDrawable(this@OnboardingActivity, R.drawable.ic_arrow_forward)

                        binding.btnNext.alpha = 0f
                        binding.btnNext.animate().alpha(1f).setDuration(150).start()
                    }
                })
                start()
            }
        }
    }

    private fun completeOnboarding() {
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isOnboardingCompleted", true).apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    class OnboardingPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            page.apply {
                val r = 1 - Math.abs(position)
                alpha = 0.3f + 0.7f * r

                val cardIllustration = findViewById<View>(com.example.neyza_insight.R.id.cardIllustration)
                val tvTitle = findViewById<View>(com.example.neyza_insight.R.id.tvTitle)
                val tvDescription = findViewById<View>(com.example.neyza_insight.R.id.tvDescription)

                if (cardIllustration != null) {
                    cardIllustration.translationX = position * (width * 0.5f)
                    cardIllustration.scaleX = 0.85f + 0.15f * r
                    cardIllustration.scaleY = 0.85f + 0.15f * r
                }
                if (tvTitle != null) {
                    tvTitle.translationX = position * (width * 0.7f)
                }
                if (tvDescription != null) {
                    tvDescription.translationX = position * (width * 0.9f)
                }
            }
        }
    }
}
