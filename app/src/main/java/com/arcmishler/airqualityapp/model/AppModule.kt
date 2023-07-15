package com.arcmishler.airqualityapp.model

import com.arcmishler.airqualityapp.api.AirPollutionAPIService
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
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
//
//    @Provides
//    fun provideGeoCodingApiService(retrofit: Retrofit): GeoCodingApiService {
//        return retrofit.create(GeoCodingApiService::class.java)
//    }

    @Provides
    fun provideAirPollutionApiService(retrofit: Retrofit): AirPollutionAPIService {
        return retrofit.create(AirPollutionAPIService::class.java)
    }
}