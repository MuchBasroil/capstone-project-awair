package com.example.capstoneemissioncarbon.ui.sign

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.capstoneemissioncarbon.LoginActivity
import com.example.capstoneemissioncarbon.R
import com.example.capstoneemissioncarbon.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Data untuk slide splash screen
        val splashData = listOf(
            SplashItem(R.drawable.logo_profile, "Welcome", ""),
            SplashItem(R.drawable.kincir1, "Udara Sehat", "Aplikasi Polusi Udara untuk Kehidupan Lebih Baik"),
            SplashItem(R.drawable.kincir2, "Lindungi Diri", "Informasi Polusi Udara Real-Time di Genggamanmu"),
            SplashItem(R.drawable.kincir3, "Hidup Sehat", "Hidup Lebih Sehat dengan Udara Segar")
        )

        // Setup ViewPager2 dengan adapter
        val adapter = SplashAdapter(splashData)
        binding.viewPager.adapter = adapter

        // Tambahkan garis indikator secara dinamis
        addLineIndicators(splashData.size)

        // Tombol "Skip"
        binding.btnSkip.setOnClickListener {
            navigateToLogin()
        }

        // Tombol "Next" / "Finish"
        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem < splashData.size - 1) {
                binding.viewPager.currentItem += 1
            } else {
                navigateToLogin()
            }
        }

        // Perbarui garis indikator saat slide berganti
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateLineIndicators(position)
                binding.btnNext.text = if (position == splashData.size - 1) "Finish" else "Next"
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun addLineIndicators(count: Int) {
        val layout = binding.lineIndicator
        layout.removeAllViews() // Bersihkan garis sebelumnya

        for (i in 0 until count) {
            val line = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f).apply {
                    marginEnd = if (i < count - 1) 8 else 0
                }
                background = ContextCompat.getDrawable(context, R.color.gray) // Warna default (abu-abu)
            }
            layout.addView(line)
        }
    }

    private fun updateLineIndicators(position: Int) {
        val layout = binding.lineIndicator
        for (i in 0 until layout.childCount) {
            val line = layout.getChildAt(i)
            line.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    if (i <= position) R.color.teal_200 else R.color.gray // Warna aktif atau default
                )
            )
        }
    }
}
