package com.arcmishler.airqualityapp.model

import com.arcmishler.airqualityapp.api.AirPollutionAPIService
import com.arcmishler.airqualityapp.api.GeoCodingAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        // Customize and provide OkHttp client
        return OkHttpClient.Builder()
            // Add any necessary interceptors or configurations
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideGeoCodingApiService(retrofit: Retrofit): GeoCodingAPIService {
        return retrofit.create(GeoCodingAPIService::class.java)
    }

    @Provides
    fun provideAirPollutionApiService(retrofit: Retrofit): AirPollutionAPIService {
        return retrofit.create(AirPollutionAPIService::class.java)
    }
}