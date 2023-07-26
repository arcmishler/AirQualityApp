package com.arcmishler.airqualityapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arcmishler.airqualityapp.api.AirQualityAPIService
import com.arcmishler.airqualityapp.api.GeoCodingAPIService
import com.arcmishler.airqualityapp.model.AirQualityResponse
import com.arcmishler.airqualityapp.model.GeoCodeResponse
import com.arcmishler.airqualityapp.model.Pollutant
import com.arcmishler.airqualityapp.model.PollutantType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val geoCodingApi: GeoCodingAPIService,
    private val airQualityAPI: AirQualityAPIService
) : ViewModel() {
    private val apiKey = "db36d01a9dda35a2c53e8caf78476bef"
    private val apiNinjaKey = "mv0ZEM82enoM1zRRLtNzCQ==q2P4mekhBkPxLSzT"

    private var _pollutantList = MutableStateFlow<List<Pollutant>?>(null)
    val pollutantList: StateFlow<List<Pollutant>?> = _pollutantList

    private var _aqiData = MutableStateFlow<Int?>(null)
    val aqiData: StateFlow<Int?> = _aqiData

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
//                        fetchPollutants(it.lat, it.lon)
//                        fetchAQI(it.lat, it.lon)
                        fetchAirQuality(it.lat, it.lon)
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error with GeoCode call: ", e)
            }
        }
    }

    private fun fetchAirQuality(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = airQualityAPI.getAirQualityData(lat, lon, apiNinjaKey)
                if (response.isSuccessful) {
                    if (response.raw().cacheResponse != null) {
                        Log.d("Cache Debug", "Response from cache")
                    } else if (response.raw().networkResponse != null) {
                        Log.d("Cache Debug", "Response from network")
                    }
                    val airQualityResponse: AirQualityResponse? = response.body()
                    airQualityResponse?.let {
                        // Extract overall AQI from the response
                        val overallAqi = it.overallAqi

                        // Create a list of pollutants using the AQI data
                        val pollutants = listOf(
                            Pollutant(PollutantType.CO, it.co.concentration),
                            Pollutant(PollutantType.NO2, it.no2.concentration),
                            Pollutant(PollutantType.O3, it.o3.concentration),
                            Pollutant(PollutantType.SO2, it.so2.concentration),
                            Pollutant(PollutantType.PM25, it.pm25.concentration),
                            Pollutant(PollutantType.PM10, it.pm10.concentration)
                        )

                        // Now, you can use the overall AQI and the list of pollutants as needed
                        _pollutantList.value = pollutants
                        _aqiData.value = overallAqi
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error with AirQuality call: ", e)
            }
        }
    }

//    private fun fetchPollutants(lat: Double, lon: Double) {
//        viewModelScope.launch {
//            try {
//                val response = airPollutionApi.getAirPollutionData(lat, lon, apiKey)
//                if (response.isSuccessful) {
//                    val airPollutionResponse: AirPollutionResponse? = response.body()
//                    val airQualityList: List<AirQuality>? = airPollutionResponse?.airQualityList
//                    val airQuality: AirQuality? = airQualityList?.get(0) // Assuming there's only one item in the list
//                    airQuality?.let {
//                        val filteredComponents = filterComponents(it.components)
//                        val pollutants = filteredComponents.map { (component, value) ->
//                            val pollutantType = PollutantType.valueOf(component.uppercase())
//                            Pollutant(pollutantType, value)
//                        }
//                        // Now, you can use the list of pollutants as needed
//                        // For example, you can store it in _pollutantList
//                        _pollutantList.value = pollutants
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("ViewModel", "Error with AirPollution call: ", e)
//            }
//        }
//    }
//
//
//
//    private fun fetchAQI(lat: Double, lon: Double) {
//        viewModelScope.launch {
//            try {
//                val response = aqiAPI.getAqiData(lat, lon, apiNinjaKey)
//                Log.d("AQI ViewModel", "Response: ${response.raw().toString()}") // Log the raw response
//
//                if (response.isSuccessful) {
//                    val aqiResponse: AqiResponse? = response.body()
//                    _aqiData.value = aqiResponse
//                    Log.d("AQI ViewModel", "AQI Response: $aqiResponse")
//                } else {
//                    Log.e("AQI ViewModel", "API call unsuccessful: ${response.code()} - ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("AQI ViewModel", "Error with API call: ", e)
//            }
//        }
//    }
}