package com.arcmishler.airqualityapp.model

import android.app.Application
import com.arcmishler.airqualityapp.api.APIClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AirQualityApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val airPollutionAPIService = APIClient.createAirPollutionAPIService(applicationContext)
        val geoCodingAPIService = APIClient.createGeoCodingAPIService(applicationContext)
        val airQualityAPIService = APIClient.createAirQualityAPIService(applicationContext)
    }
}