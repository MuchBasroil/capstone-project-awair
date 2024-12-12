package com.example.capstoneemissioncarbon.ui.predict

import com.google.gson.annotations.SerializedName

data class PredictionRequest(
    @SerializedName("data")
    val data: List<Float>  // Data input dalam bentuk list angka (float)
)
