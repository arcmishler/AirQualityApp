package com.arcmishler.airqualityapp.api

import com.arcmishler.airqualityapp.model.AirQualityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AirQualityAPIService {
    @GET("v1/airquality")
    suspend fun getAirQualityData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Header("X-Api-Key") apiKey: String
    ): Response<AirQualityResponse>
}