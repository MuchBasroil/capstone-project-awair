package com.example.capstoneemissioncarbon.ui.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL_1 = "https://carbon-prediction-api-542840898498.us-central1.run.app/"
    private const val BASE_URL_2 = "https://carbon-prediction-api-ranforst-542840898498.us-central1.run.app/"

    private fun createRetrofit(baseUrl: String): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService1: ApiService by lazy {
        createRetrofit(BASE_URL_1).create(ApiService::class.java)
    }

    val apiService2: ApiService by lazy {
        createRetrofit(BASE_URL_2).create(ApiService::class.java)
    }
}
