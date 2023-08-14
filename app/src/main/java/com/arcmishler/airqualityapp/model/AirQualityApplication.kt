package com.arcmishler.airqualityapp.model

import android.app.Application
import com.arcmishler.airqualityapp.api.APIClient
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AirQualityApplication: Application() {
    @Inject
    lateinit var airQualityDatabase: AirQualityDatabase
    override fun onCreate() {
        super.onCreate()

        val geoCodingAPIService = APIClient.createGeoCodingAPIService(applicationContext)
        val airQualityAPIService = APIClient.createAirQualityAPIService(applicationContext)


    }
}