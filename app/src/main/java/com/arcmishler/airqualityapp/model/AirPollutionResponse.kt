package com.arcmishler.airqualityapp.model

import com.google.gson.annotations.SerializedName

data class AirPollutionResponse(
    @SerializedName("list") val airQualityList: List<AirQuality>
)

data class AirQuality(
    @SerializedName("main") val main: MainData,
    @SerializedName("components") val components: ComponentsData
)

data class MainData(
    @SerializedName("aqi") val aqi: Int
)

data class ComponentsData(
    @SerializedName("co") val co: Double,
    @SerializedName("no2") val no2: Double,
    @SerializedName("o3") val o3: Double,
    @SerializedName("so2") val so2: Double,
    @SerializedName("pm2_5") val pm25: Double,
    @SerializedName("pm10") val pm10: Double
)