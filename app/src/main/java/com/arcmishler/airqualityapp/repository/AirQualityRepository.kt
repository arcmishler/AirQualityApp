package com.arcmishler.airqualityapp.repository

import com.arcmishler.airqualityapp.api.AirQualityAPIService
import com.arcmishler.airqualityapp.api.GeoCodingAPIService
import com.arcmishler.airqualityapp.model.AirQualityResponse
import com.arcmishler.airqualityapp.model.GeoCodeResponse
import javax.inject.Inject

class AirQualityRepository @Inject constructor(
    private val geoCodingApi: GeoCodingAPIService,
    private val airQualityApi: AirQualityAPIService
) {
    private val apiKey = "db36d01a9dda35a2c53e8caf78476bef"
    private val apiNinjaKey = "mv0ZEM82enoM1zRRLtNzCQ==q2P4mekhBkPxLSzT"

    suspend fun getGeoCode(zip: String): GeoCodeResponse? {
        val response = geoCodingApi.getGeoCodeData(zip, apiKey)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun getAirQuality(lat: Double, lon: Double): AirQualityResponse? {
        val response = airQualityApi.getAirQualityData(lat, lon, apiNinjaKey)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}