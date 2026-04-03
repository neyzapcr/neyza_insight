package com.example.neyza_insight.pertemuan_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME") ?: "User"

        binding.tvWelcomeUser.text = getString(R.string.welcome_user, username)
    }
}