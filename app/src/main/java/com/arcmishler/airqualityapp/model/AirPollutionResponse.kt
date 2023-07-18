package com.arcmishler.airqualityapp.model

import com.google.gson.annotations.SerializedName

data class AirPollutionResponse(
    @SerializedName("list") val airQualityList: List<AirQuality>
)

data class AirQuality(
    @SerializedName("main") val main: MainData,
    @SerializedName("components") val components: Map<String, Double>
)

data class MainData(
    @SerializedName("aqi") val aqi: Int
)