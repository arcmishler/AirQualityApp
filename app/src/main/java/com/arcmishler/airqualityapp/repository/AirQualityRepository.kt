package com.arcmishler.airqualityapp.repository

import com.arcmishler.airqualityapp.BuildConfig
import com.arcmishler.airqualityapp.api.AirQualityAPIService
import com.arcmishler.airqualityapp.api.GeoCodingAPIService
import com.arcmishler.airqualityapp.model.AirQualityDAO
import com.arcmishler.airqualityapp.model.AirQualityResponse
import com.arcmishler.airqualityapp.model.AirQualityTable
import com.arcmishler.airqualityapp.model.GeoCodeResponse
import com.google.gson.Gson
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AirQualityRepository @Inject constructor(
    private val geoCodingApi: GeoCodingAPIService,
    private val airQualityApi: AirQualityAPIService,
    private val airQualityDao: AirQualityDAO
) {
    private val apiKeyOpen = BuildConfig.API_KEY_OPEN
    private val apiKeyNinja = BuildConfig.API_KEY_NINJA

    /**
     * Returns a [GeoCodeResponse] that contains lat and lon associated with [zip].
     */
    suspend fun getGeoCode(zip: String): GeoCodeResponse? {
        val response = geoCodingApi.getGeoCodeData(zip, apiKeyOpen)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    /**
     * Returns either a cached version of [AirQualityResponse] if it has been cached within the hour,
     * or returns a new response from API call and inserts into the table
     * if a call has not been made within the hour.
     */
    suspend fun getAirQuality(lat: Double, lon: Double): AirQualityResponse? {

        val cachedAirQuality = airQualityDao.getCachedAirQuality(lat, lon, System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))

        // Return cached response if within the hour interval of last call
        if(cachedAirQuality != null && isValidData(cachedAirQuality.timestamp)) {
            return Gson().fromJson(cachedAirQuality.responseJson, AirQualityResponse::class.java)
        } else {
            // Return fresh API response and insert into table if not within the hour
            val response = airQualityApi.getAirQualityData(lat, lon, apiKeyNinja)
            if (response.isSuccessful) {
                val responseJson = Gson().toJson(response.body())
                airQualityDao.insertAirQuality(
                    AirQualityTable(0, lat, lon, responseJson, System.currentTimeMillis(),)
                )
                return response.body()
            }
            return null
        }
    }

    /**
     * Checks if [timestamp] is still within the hour or not.
     */
    private fun isValidData(timestamp: Long): Boolean {
        val currTime = System.currentTimeMillis()
        val hourInterval = TimeUnit.HOURS.toMillis(1)

        return (currTime - timestamp) < hourInterval
    }
}