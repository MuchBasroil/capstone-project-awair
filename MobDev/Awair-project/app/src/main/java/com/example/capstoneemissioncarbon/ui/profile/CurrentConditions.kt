package com.example.capstoneemissioncarbon.ui.profile

import com.google.gson.annotations.SerializedName

data class CurrentConditions(
    val title: String,
    val description: String,
    var date: String,
    val status: String

)

// Respons API untuk kualitas udara secara detail
data class AirQualityResponse(
    val dateTime: String,
    val regionCode: String,
    val indexes: List<Index>,
    val pollutants: List<Pollutant>,
    val healthRecommendations: HealthRecommendations
)

// Data tentang indeks kualitas udara
data class Index(
    val code: String,
    val displayName: String,
    val aqi: Int,
    val aqiDisplay: String,
    val color: Color,
    val category: String,
    val dominantPollutant: String
)

// Data tentang warna yang digunakan untuk indeks kualitas udara
data class Color(
    val red: Float?,
    val green: Float?,
    val blue: Float?
)

// Data tentang polutan yang mempengaruhi kualitas udara
data class Pollutant(
    val code: String,
    val displayName: String,
    val fullName: String,
    val concentration: Concentration,
    val additionalInfo: AdditionalInfo
)

// Data tentang konsentrasi polutan
data class Concentration(
    val value: Double,
    val units: String
)

// Data tambahan tentang polutan
data class AdditionalInfo(
    val sources: String,
    val effects: String
)

// Rekomendasi kesehatan berdasarkan kualitas udara
data class HealthRecommendations(
    val generalPopulation: String,
    val elderly: String,
    val lungDiseasePopulation: String,
    val heartDiseasePopulation: String,
    val athletes: String,
    val pregnantWomen: String,
    val children: String
)

data class LstmRequest(
    val input: List<List<List<Double>>>
)

data class LstmResponse(
    val prediction: List<List<List<Double>>>
)

data class CarbonRequest(
    val input: List<Double>
)

    data class RandomForestRequest(
val input: List<List<Double>> // Example input format; adjust as needed
    )

// RandomForestResponse class (adjust according to your API)
data class RandomForestResponse(
    val prediction: Double? // Adjust the field name and type according to your response
)

data class HistoryItem(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: String
)

data class Prediction(
    val City: String?,
    @SerializedName("Dom. Pollutant") val DominantPollutant: String?,
    val ConsentrationLevels: Double? = null
)

data class CarbonResponse(
    val prediction: Prediction
)

data class LinearRequest(
    val input: List<Double> // Format baru hanya menggunakan list Double, bukan list of list
)

data class LinearResponse(
    val prediction: List<List<Double>> // Sesuaikan dengan response body
)







