package com.example.capstoneemissioncarbon.ui.retrofit

import com.example.capstoneemissioncarbon.ui.profile.CarbonRequest
import com.example.capstoneemissioncarbon.ui.profile.CarbonResponse
import com.example.capstoneemissioncarbon.ui.profile.CurrentConditions
import com.example.capstoneemissioncarbon.ui.profile.HistoryItem
import com.example.capstoneemissioncarbon.ui.profile.LinearRequest
import com.example.capstoneemissioncarbon.ui.profile.LinearResponse
import com.example.capstoneemissioncarbon.ui.profile.LstmRequest
import com.example.capstoneemissioncarbon.ui.profile.LstmResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // Endpoint untuk LSTM Prediction
    @POST("predict-lstm")
    fun predictLstm(@Body request: LstmRequest): Call<LstmResponse>

    // Endpoint untuk Linear Prediction
    @POST("predict-linear")
    fun predictLinear(@Body request: LinearRequest): Call<LinearResponse>

    // Endpoint untuk Carbon Prediction (Random Forest)
    @POST("predict-rf")
    fun predictCarbon(@Body request: CarbonRequest): Call<CarbonResponse>

    @GET("history") // Sesuaikan endpoint API Anda
    fun getHistoryData(): Call<List<CurrentConditions>>
    }




