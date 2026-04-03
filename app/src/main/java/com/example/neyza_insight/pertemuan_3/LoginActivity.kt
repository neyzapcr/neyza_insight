package com.example.neyza_insight.pertemuan_3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty()) {
                binding.etUsername.error = "Username wajib diisi"
                binding.etUsername.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Password wajib diisi"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }
    }
}