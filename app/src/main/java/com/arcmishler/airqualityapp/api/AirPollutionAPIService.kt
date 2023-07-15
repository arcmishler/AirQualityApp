package com.arcmishler.airqualityapp.api

import com.arcmishler.airqualityapp.model.AirPollutionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirPollutionAPIService {
    @GET("data/2.5/air_pollution")
    suspend fun getAirPollutionData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<AirPollutionResponse>
}