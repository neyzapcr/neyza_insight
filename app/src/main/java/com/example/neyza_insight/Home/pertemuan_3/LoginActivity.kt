package com.example.neyza_insight.Home.pertemuan_3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.BaseActivity
import com.example.neyza_insight.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // ke register
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {

            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // VALIDASI INPUT KOSONG
            if (username.isEmpty()) {
                binding.etUsername.error = "Wajib"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Wajib"
                return@setOnClickListener
            }

            val savedUsername = sharedPref.getString("username", null)
            val savedPassword = sharedPref.getString("password", null)

            // CEK BELUM REGISTER
            if (savedUsername.isNullOrEmpty() || savedPassword.isNullOrEmpty()) {
                Toast.makeText(this, "Belum register akun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // RULE LOGIN (SESUAI SOAL)
            val loginValid =
                (username == password) ||
                        (username == savedUsername && password == savedPassword)

            if (loginValid) {

                sharedPref.edit().putBoolean("isLogin", true).apply()

                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, BaseActivity::class.java))
                finish()

            } else {
                Toast.makeText(this, "Username atau Password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}