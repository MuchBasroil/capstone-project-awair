package com.example.capstoneemissioncarbon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstoneemissioncarbon.databinding.ActivityLoginBinding
import com.example.capstoneemissioncarbon.ui.sign.ForgotPasswordActivity
import android.content.SharedPreferences

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Login
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(username, password)) {
                // Mengambil data pengguna dari SharedPreferences
                val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
                val storedUsername = sharedPreferences.getString("username", null)
                val storedPassword = sharedPreferences.getString("password", null)

                // Cek apakah username dan password cocok
                if (username == storedUsername && password == storedPassword) {
                    Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol "Sign Up"
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tombol "Forgot Password"
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi validasi input
    private fun validateInput(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}
