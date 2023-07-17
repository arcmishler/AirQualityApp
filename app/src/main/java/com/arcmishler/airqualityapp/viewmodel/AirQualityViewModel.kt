package com.arcmishler.airqualityapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arcmishler.airqualityapp.api.AirPollutionAPIService
import com.arcmishler.airqualityapp.api.GeoCodingAPIService
import com.arcmishler.airqualityapp.model.AirPollutionResponse
import com.arcmishler.airqualityapp.model.AirQuality
import com.arcmishler.airqualityapp.model.GeoCodeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val airPollutionApi: AirPollutionAPIService,
    private val geoCodingApi: GeoCodingAPIService
) : ViewModel() {
    private val apiKey = "db36d01a9dda35a2c53e8caf78476bef"

    private var _airQualityData = MutableStateFlow<AirQuality?>(null)
    val airQualityData: StateFlow<AirQuality?> = _airQualityData

    private var _geoCodeData = MutableStateFlow<GeoCodeResponse?>(null)
    val geoCodeData: StateFlow<GeoCodeResponse?> = _geoCodeData

    fun fetchGeoCode(zip: String) {
        viewModelScope.launch {
            try {
                val response = geoCodingApi.getGeoCodeData(zip, apiKey)
                if (response.isSuccessful) {
                    val geoCodeResponse: GeoCodeResponse? = response.body()
                    _geoCodeData.value = geoCodeResponse
                    geoCodeResponse?.let {
                        fetchAirQuality(it.lat, it.lon)
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error with GeoCode call: ", e)
            }
        }
    }
    fun fetchAirQuality(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = airPollutionApi.getAirPollutionData(lat, lon, apiKey)
                if (response.isSuccessful) {
                    val airPollutionResponse: AirPollutionResponse? = response.body()
                    val airQualityList: List<AirQuality>? = airPollutionResponse?.airQualityList
                    val airQuality: AirQuality? = airQualityList?.get(0) // Assuming there's only one item in the list
                    _airQualityData.value = airQuality
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error with AirPollution call: ", e)
            }

        }
    }
}