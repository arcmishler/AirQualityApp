package com.arcmishler.airqualityapp.api

import com.arcmishler.airqualityapp.model.GeoCodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingAPIService {
    @GET("geo/1.0/zip")
    suspend fun getGeoCodeData(
        @Query("zip") zip: String,
        @Query("appid") apiKey: String
    ): Response<GeoCodeResponse>
}