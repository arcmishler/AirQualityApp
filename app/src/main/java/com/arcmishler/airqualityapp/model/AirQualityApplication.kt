package com.arcmishler.airqualityapp.model

import android.app.Application
import com.arcmishler.airqualityapp.api.APIClient

class AirQualityApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val airPollutionAPIService = APIClient.createAirPollutionAPIService(applicationContext)
    }
}