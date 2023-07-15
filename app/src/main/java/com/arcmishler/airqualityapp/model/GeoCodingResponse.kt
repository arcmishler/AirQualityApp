package com.arcmishler.airqualityapp.model

import com.google.gson.annotations.SerializedName

data class GeoCodingResponse(
    @SerializedName("zip") val zip: Int,
    @SerializedName("name") val name: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("country") val country: String,
)
