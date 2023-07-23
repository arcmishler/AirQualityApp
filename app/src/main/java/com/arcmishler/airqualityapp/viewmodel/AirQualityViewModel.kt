package com.arcmishler.airqualityapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arcmishler.airqualityapp.api.AirPollutionAPIService
import com.arcmishler.airqualityapp.api.AqiAPIService
import com.arcmishler.airqualityapp.api.GeoCodingAPIService
import com.arcmishler.airqualityapp.model.AirPollutionResponse
import com.arcmishler.airqualityapp.model.AirQuality
import com.arcmishler.airqualityapp.model.AqiResponse
import com.arcmishler.airqualityapp.model.GeoCodeResponse
import com.arcmishler.airqualityapp.model.Pollutant
import com.arcmishler.airqualityapp.model.PollutantRanges
import com.arcmishler.airqualityapp.model.filterComponents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val airPollutionApi: AirPollutionAPIService,
    private val geoCodingApi: GeoCodingAPIService,
    private val aqiAPI: AqiAPIService
) : ViewModel() {
    private val apiKey = "db36d01a9dda35a2c53e8caf78476bef"
    private val apiNinjaKey = "mv0ZEM82enoM1zRRLtNzCQ==q2P4mekhBkPxLSzT"

    private var _pollutantList = MutableStateFlow<List<Pollutant>?>(null)
    val pollutantList: StateFlow<List<Pollutant>?> = _pollutantList

    private var _aqiData = MutableStateFlow<AqiResponse?>(null)
    val aqiData: StateFlow<AqiResponse?> = _aqiData

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
                        fetchPollutants(it.lat, it.lon)
                        fetchAQI(it.lat, it.lon)
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error with GeoCode call: ", e)
            }
        }
    }
    fun fetchPollutants(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = airPollutionApi.getAirPollutionData(lat, lon, apiKey)
                if (response.isSuccessful) {
                    val airPollutionResponse: AirPollutionResponse? = response.body()
                    val airQualityList: List<AirQuality>? = airPollutionResponse?.airQualityList
                    val airQuality: AirQuality? = airQualityList?.get(0) // Assuming there's only one item in the list
                    airQuality?.let {
                        val filteredComponents = filterComponents(it.components)
                        val pollutants = filteredComponents.map { (component, value) ->
                            val pollutantLevels = PollutantRanges().getColorRange(component)
                            Pollutant(component, value, pollutantLevels)
                        }
                        // Now, you can use the list of pollutants as needed
                        // For example, you can store it in _airQualityData
                        _pollutantList.value = pollutants
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error with AirPollution call: ", e)
            }
        }
    }


    fun fetchAQI(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = aqiAPI.getAqiData(lat, lon, apiNinjaKey)
                Log.d("AQI ViewModel", "Response: ${response.raw().toString()}") // Log the raw response

                if (response.isSuccessful) {
                    val aqiResponse: AqiResponse? = response.body()
                    _aqiData.value = aqiResponse
                    Log.d("AQI ViewModel", "AQI Response: $aqiResponse")
                } else {
                    Log.e("AQI ViewModel", "API call unsuccessful: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AQI ViewModel", "Error with API call: ", e)
            }
        }
    }
}