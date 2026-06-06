package com.example.neyza_insight

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.neyza_insight.Home.pertemuan_3.LoginActivity
import com.example.neyza_insight.Home.pertemuan_4.DashboardActivity
import com.example.neyza_insight.Onboarding.OnboardingActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.jvm.java

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val isLogin = sharedPref.getBoolean("isLogin", false)
        val isOnboardingCompleted = sharedPref.getBoolean("isOnboardingCompleted", false)

        lifecycleScope.launch {
            delay(2000)

            val intent = if (isLogin) {
                Intent(this@SplashScreenActivity, BaseActivity::class.java)
            } else if (isOnboardingCompleted) {
                Intent(this@SplashScreenActivity, LoginActivity::class.java)
            } else {
                Intent(this@SplashScreenActivity, OnboardingActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}