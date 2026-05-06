package com.example.neyza_insight.Home.pertemuan_3

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.neyza_insight.databinding.ActivityRegisterBinding
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Spinner
        val agamaList = arrayOf("Pilih Agama", "Islam", "Kristen", "Hindu", "Budha")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, agamaList)
        binding.spinnerAgama.adapter = adapter

        // DatePicker
        binding.etTanggalLahir.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                { _, y, m, d ->
                    binding.etTanggalLahir.setText("$d/${m + 1}/$y")
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnRegister.setOnClickListener {

            val nama = binding.etNama.text.toString()
            val ttl = binding.etTanggalLahir.text.toString()
            val genderId = binding.radioGroupGender.checkedRadioButtonId
            val agama = binding.spinnerAgama.selectedItem.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()

            var valid = true

            if (nama.isEmpty()) {
                binding.etNama.error = "Wajib"
                valid = false
            }

            if (ttl.isEmpty()) {
                binding.etTanggalLahir.error = "Wajib"
                valid = false
            }

            if (genderId == -1) {
                Toast.makeText(this, "Pilih gender", Toast.LENGTH_SHORT).show()
                valid = false
            }

            if (agama == "Pilih Agama") {
                Toast.makeText(this, "Pilih agama", Toast.LENGTH_SHORT).show()
                valid = false
            }

            if (username.isEmpty()) {
                binding.etUsername.error = "Wajib"
                valid = false
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Wajib"
                valid = false
            }

            if (confirm.isEmpty()) {
                binding.etConfirmPassword.error = "Wajib"
                valid = false
            }

            if (password != confirm) {
                binding.etConfirmPassword.error = "Tidak sama"
                valid = false
            }

            if (!valid) return@setOnClickListener

            val gender = findViewById<RadioButton>(genderId).text.toString()

            sharedPref.edit()
                .putString("nama", nama)
                .putString("ttl", ttl)
                .putString("gender", gender)
                .putString("agama", agama)
                .putString("username", username)
                .putString("password", password)
                .apply()

            Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}