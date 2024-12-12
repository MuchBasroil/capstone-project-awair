package com.example.capstoneemissioncarbon.ui.predict

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("prediction")
    val prediction: Double?  // Hasil prediksi dalam bentuk angka float
)
