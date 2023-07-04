package com.arcmishler.airqualityapp.model

import com.google.gson.annotations.SerializedName

data class AirPollutionResponse(
    @SerializedName("list") val airQualityList: List<AirQuality>
)

data class AirQuality(
    @SerializedName("aqi") val aqi: Int,
    @SerializedName("components") val components: Map<String, Double>
)