package com.arcmishler.airqualityapp.api

import com.arcmishler.airqualityapp.model.AqiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AqiAPIService {
    @GET("v1/airquality")
    suspend fun getAqiData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Header("X-Api-Key") apiKey: String
    ): Response<AqiResponse>
}