package com.arcmishler.airqualityapp.model

import com.google.gson.annotations.SerializedName

data class AqiResponse(
    @SerializedName("CO") val co: AqiData,
    @SerializedName("NO2") val no2: AqiData,
    @SerializedName("O3") val o3: AqiData,
    @SerializedName("SO2") val so2: AqiData,
    @SerializedName("PM2.5") val pm25: AqiData,
    @SerializedName("PM10") val pm10: AqiData,
    @SerializedName("overall_aqi") val overallAqi: Int
)

data class AqiData(
    @SerializedName("concentration") val concentration: Double,
    @SerializedName("aqi") val aqi: Int
)
