package com.arcmishler.airqualityapp.model

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("overall_aqi") val overallAqi: Int,
    @SerializedName("CO") val co: AQIData,
    @SerializedName("NO2") val no2: AQIData,
    @SerializedName("O3") val o3: AQIData,
    @SerializedName("SO2") val so2: AQIData,
    @SerializedName("PM2.5") val pm25: AQIData,
    @SerializedName("PM10") val pm10: AQIData
)

data class AQIData(
    @SerializedName("concentration") val concentration: Double,
    @SerializedName("aqi") val aqi: Int
)