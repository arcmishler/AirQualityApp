package com.arcmishler.airqualityapp.api

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


object APIClient {
    private const val BASE_URL = "https://api.openweathermap.org/"
    private const val CACHE_SIZE = 10 * 1024 * 1024

    fun createAirPollutionAPIService(applicationContext: Context): AirPollutionAPIService {
        val okHttpClient = OkHttpClient.Builder()
            .cache(createCache(applicationContext))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AirPollutionAPIService::class.java)
    }

    private fun createCache(applicationContext: Context): Cache {
        return Cache(
            File(applicationContext.cacheDir, "api_cache"),
            CACHE_SIZE.toLong()
        )
    }
}