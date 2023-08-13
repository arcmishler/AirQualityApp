package com.arcmishler.airqualityapp.repository

import com.arcmishler.airqualityapp.BuildConfig
import com.arcmishler.airqualityapp.api.AirQualityAPIService
import com.arcmishler.airqualityapp.api.GeoCodingAPIService
import com.arcmishler.airqualityapp.model.AirQualityResponse
import com.arcmishler.airqualityapp.model.GeoCodeResponse
import javax.inject.Inject

class AirQualityRepository @Inject constructor(
    private val geoCodingApi: GeoCodingAPIService,
    private val airQualityApi: AirQualityAPIService
) {
    private val apiKeyOpen = BuildConfig.API_KEY_OPEN
    private val apiKeyNinja = BuildConfig.API_KEY_NINJA

    suspend fun getGeoCode(zip: String): GeoCodeResponse? {
        val response = geoCodingApi.getGeoCodeData(zip, apiKeyOpen)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun getAirQuality(lat: Double, lon: Double): AirQualityResponse? {
        val response = airQualityApi.getAirQualityData(lat, lon, apiKeyNinja)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}