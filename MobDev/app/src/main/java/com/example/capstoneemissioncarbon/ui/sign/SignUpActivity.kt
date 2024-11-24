package com.example.capstoneemissioncarbon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstoneemissioncarbon.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi View Binding
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol "CREATE"
        binding.btnCreate.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            // Validasi input sebelum lanjut
            if (validateInput(username, email, phone, password, confirmPassword)) {
                // Contoh aksi: tampilkan pesan sukses dan pindah ke MainActivity
                Toast.makeText(this, "Sign Up Berhasil", Toast.LENGTH_SHORT).show()

                // Pindah ke MainActivity setelah sign up berhasil
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Mengakhiri activity Signup untuk mencegah kembali ke halaman Signup
            }
        }

        // Teks "Login here" untuk redirect ke halaman login atau MainActivity
        binding.tvLogin.setOnClickListener {
            // Misalnya pindah ke MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // Validasi input pengguna
    private fun validateInput(
        username: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        // Cek apakah ada field yang kosong
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show()
            return false
        }

        // Cek apakah password dan confirm password cocok
        if (password != confirmPassword) {
            Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        }

        // Jika semua validasi berhasil, return true
        return true
    }
}
