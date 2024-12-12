package com.example.capstoneemissioncarbon.ui.sign

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstoneemissioncarbon.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    // View binding untuk mengakses elemen UI
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol "Send Instruction"
        binding.btnSendInstruction.setOnClickListener {
            val email = binding.etEmail.text.toString()

            // Validasi input email
            if (email.isEmpty()) {
                Toast.makeText(this, "Harap masukkan email Anda", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            } else {
                // Contoh notifikasi reset password
                Toast.makeText(this, "Instruksi reset password telah dikirim ke $email", Toast.LENGTH_SHORT).show()

                // Tambahkan logika berikut untuk mengirim permintaan ke API (opsional)
                // sendPasswordResetRequest(email)
            }
        }
    }

    // Fungsi untuk memvalidasi format email
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Contoh metode untuk mengirim permintaan reset password (opsional, bisa gunakan API)
    private fun sendPasswordResetRequest(email: String) {
        // Implementasikan logika permintaan ke server untuk mengirim email reset password
        // Contoh: Gunakan Retrofit atau library jaringan lainnya
    }
}
