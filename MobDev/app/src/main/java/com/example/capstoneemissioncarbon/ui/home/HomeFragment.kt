package com.example.capstoneemissioncarbon.ui.home

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstoneemissioncarbon.R
import com.example.capstoneemissioncarbon.databinding.FragmentHomeBinding
import com.example.capstoneemissioncarbon.ui.profile.CarbonRequest
import com.example.capstoneemissioncarbon.ui.profile.CarbonResponse
import com.example.capstoneemissioncarbon.ui.profile.LinearRequest
import com.example.capstoneemissioncarbon.ui.profile.LinearResponse
import com.example.capstoneemissioncarbon.ui.profile.LstmRequest
import com.example.capstoneemissioncarbon.ui.profile.LstmResponse
import com.example.capstoneemissioncarbon.ui.profile.Prediction
import com.example.capstoneemissioncarbon.ui.retrofit.ApiClient
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateStaticUI()
        fetchLstmPrediction()
        fetchLinearPrediction()
        fetchRandomForestData()
        loadUserProfile()
    }


    private fun loadImageFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun cropToCircle(bitmap: Bitmap): Bitmap {
        val size = minOf(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        Canvas(output).apply {
            val paint = Paint().apply { isAntiAlias = true }
            drawOval(RectF(0f, 0f, size.toFloat(), size.toFloat()), paint)
            paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
            drawBitmap(bitmap, null, RectF(0f, 0f, size.toFloat(), size.toFloat()), paint)
        }
        return output
    }



    private fun loadUserProfile() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Guest")
        val profileImageUri = sharedPreferences.getString("profile_image_uri", null)

        // Perbarui UI dengan nama pengguna
        binding.username.text = "Hello, $username"
        binding.userSubtitle.text = "Check your air quality today"

        // Perbarui gambar profil jika URI tersedia
        if (!profileImageUri.isNullOrEmpty()) {
            val uri = Uri.parse(profileImageUri)
            val bitmap = loadImageFromUri(uri)
            if (bitmap != null) {
                binding.profileImage.setImageBitmap(cropToCircle(bitmap))
            } else {
                binding.profileImage.setImageResource(R.drawable.baseline_person_24) // Gambar default
            }
        } else {
            binding.profileImage.setImageResource(R.drawable.baseline_person_24) // Gambar default
        }
    }


    private val profileUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loadUserProfile() // Muat ulang data pengguna
        }
    }

    override fun onStart() {
        super.onStart()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            requireContext().registerReceiver(
                profileUpdateReceiver,
                IntentFilter("com.example.UPDATE_PROFILE"),
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            requireContext().registerReceiver(
                profileUpdateReceiver,
                IntentFilter("com.example.UPDATE_PROFILE")
            )
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(profileUpdateReceiver)
    }


    private fun fetchLstmPrediction() {
        val lstmRequest = LstmRequest(
            input = listOf(
                listOf(
                    listOf(488.99, 18.79, 84.07, 89.18, 89.18, 4.21),
                    listOf(489.34, 8.71, 89.47, 113.85, 113.85, 5.66),
                    listOf(492.09, 16.41, 78.74, 45.67, 45.67, 3.64),
                    listOf(489.48, 14.84, 82.55, 75.29, 75.29, 3.98),
                    listOf(491.03, 16.86, 82.08, 75.09, 75.09, 4.18)
                )
            )
        )

        ApiClient.apiService1.predictLstm(lstmRequest).enqueue(object : Callback<LstmResponse> {
            override fun onResponse(call: Call<LstmResponse>, response: Response<LstmResponse>) {
                if (response.isSuccessful) {
                    val predictions = response.body()?.prediction?.get(0)?.map { it[2] }
                    if (predictions != null) {
                        updateChartWithPredictions(predictions)
                    } else {
                        Toast.makeText(context, "Prediction data is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch LSTM prediction", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LstmResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateChartWithPredictions(predictions: List<Double>) {
        val entries = predictions.mapIndexed { index, value ->
            Entry(index.toFloat(), value.toFloat())
        }
        val lineDataSet = LineDataSet(entries, "LSTM Prediction")
        lineDataSet.color = Color.BLUE
        lineDataSet.valueTextColor = Color.BLACK

        val lineData = LineData(lineDataSet)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()
    }

    private fun fetchLinearPrediction() {
        // Body request diperbarui
        val linearRequest = LinearRequest(
            input = listOf(492.09, 16.41, 78.74, 45.67, 45.67, 3.64)
        )

        ApiClient.apiService1.predictLinear(linearRequest).enqueue(object : Callback<LinearResponse> {
            override fun onResponse(call: Call<LinearResponse>, response: Response<LinearResponse>) {
                if (response.isSuccessful) {
                    val prediction = response.body()?.prediction?.getOrNull(0)?.getOrNull(0) // Ambil nilai pertama
                    if (prediction != null) {
                        updateStaticUI(prediction) // Update UI dengan nilai prediction
                    } else {
                        Toast.makeText(context, "Prediction data is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch linear prediction", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LinearResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun updateStaticUI(aqi: Double = 500.8) { // Nilai default 4974.8
        _binding?.let { binding ->
            val (condition, colorResId, description) = when {
                aqi <= 50 -> Triple("Good", R.drawable.circle_green, "Air quality is satisfactory and poses little or no risk.")
                aqi in 51.0..100.0 -> Triple("Moderate", R.drawable.circle_yellow, "Air quality is acceptable; some pollutants may affect sensitive groups.")
                aqi in 101.0..150.0 -> Triple("Unhealthy for Sensitive Groups", R.drawable.circle_orange, "Sensitive groups may experience health effects. General public unlikely affected.")
                aqi in 151.0..200.0 -> Triple("Unhealthy", R.drawable.circle_red, "Everyone may experience health effects. Sensitive groups may experience serious effects.")
                aqi in 201.0..300.0 -> Triple("Very Unhealthy", R.drawable.circle_purple, "Health alert: everyone may experience serious effects.")
                else -> Triple("Hazardous", R.drawable.circle_maroon, "Health warnings of emergency conditions. The entire population may be affected.")
            }

            // Perubahan warna mengikuti gambar kedua
            binding.aqiCircle.setBackgroundResource(R.drawable.circle_aqi) // Warna hijau seperti gambar kedua
            binding.aqiValueText.text = String.format("%.1f AQI", aqi) // Masih menggunakan data dari logika/API

            // Lingkaran kanan untuk kondisi
            binding.conditionCircle.setBackgroundResource(colorResId) // Warna dinamis mengikuti kondisi
            // Warna merah seperti gambar kedua
            binding.conditionLabelText.text = condition // Masih menggunakan data dari logika/API

            // Deskripsi tetap dengan warna biru
            binding.descriptionText.text = description // Deskripsi dari logika/API
            binding.descriptionText.setBackgroundResource(R.drawable.curved_blue_background) // Warna biru seperti gambar kedua
        }
    }




    private fun fetchRandomForestData() {
        val randomForestRequest = CarbonRequest(
            input = listOf(38.0, 492.09, 16.41, 78.74, 45.67, 45.67, 3.64, -7.251514, 112.759883)
        )

        ApiClient.apiService2.predictCarbon(randomForestRequest).enqueue(object : Callback<CarbonResponse> {
            override fun onResponse(call: Call<CarbonResponse>, response: Response<CarbonResponse>) {
                if (response.isSuccessful) {
                    val prediction = response.body()?.prediction
                    if (prediction != null) {
                        updateUI(prediction)
                    } else {
                        Toast.makeText(context, "No prediction data available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch random forest data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CarbonResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(prediction: Prediction) {
        with(binding) {
            // Set lokasi
            locationText.text = prediction.City ?: "City not available"

            // Set kualitas udara
            airQuality.text = if ((prediction.ConsentrationLevels ?: 0.0) < 100) "Good" else "Bad"

            // Tangani Dominant Pollutant dengan konsentrasinya
            val domPollutantFull = prediction.DominantPollutant ?: "N/A: 0.00"
            val pollutantParts = domPollutantFull.split(":").map { it.trim() }

            // Proses data Dominant Pollutant
            val domPollutant = pollutantParts.getOrNull(0) ?: "N/A"
            val concentrationLevels = pollutantParts.getOrNull(1)?.toDoubleOrNull()?.let {
                String.format("%.2f", it)
            } ?: "0.00"

            // Gabungkan dan tampilkan di TextView
            dominantPollutant.text = "Dom. Pollutant\n$domPollutant: $concentrationLevels"
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}