package com.example.capstoneemissioncarbon

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstoneemissioncarbon.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengaktifkan ViewBinding untuk layout
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol "CREATE"
        binding.btnCreate.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()


            // Validasi input
            if (validateInput(username, email, phone, password, confirmPassword )) {
                // Menyimpan data pengguna ke SharedPreferences
                saveUserData(username, email, phone, password )

                // Menampilkan pesan sukses
                Toast.makeText(this, "Sign Up Berhasil", Toast.LENGTH_SHORT).show()

                // Pindah ke LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Mengakhiri activity Signup
            }
        }

        // Tombol "Login"
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Mengakhiri activity Signup
        }
    }

    // Fungsi untuk menyimpan data pengguna ke SharedPreferences
    private fun saveUserData(
        username: String,
        email: String,
        phone: String,
        password: String
    ) {
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.putString("password", password)
        editor.apply()
    }

    // Fungsi untuk validasi input
    private fun validateInput(
        username: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        // Cek apakah ada field yang kosong
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show()
            return false
        }

        // Cek format email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        }

        // Cek apakah password dan confirm password cocok
        if (password != confirmPassword) {
            Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validasi berhasil
        return true
    }
}
