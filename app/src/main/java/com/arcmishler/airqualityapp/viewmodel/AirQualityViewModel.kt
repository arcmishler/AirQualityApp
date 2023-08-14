package com.arcmishler.airqualityapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arcmishler.airqualityapp.model.AQI
import com.arcmishler.airqualityapp.model.AirQualityResponse
import com.arcmishler.airqualityapp.model.GeoCodeResponse
import com.arcmishler.airqualityapp.model.Pollutant
import com.arcmishler.airqualityapp.model.PollutantType
import com.arcmishler.airqualityapp.repository.AirQualityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val airQualityRepository: AirQualityRepository
) : ViewModel() {

    private var _pollutantList = MutableStateFlow<List<Pollutant>?>(null)
    val pollutantList: StateFlow<List<Pollutant>?> = _pollutantList

    private var _aqiData = MutableStateFlow<AQI?>(null)
    val aqiData: StateFlow<AQI?> = _aqiData

    private var _geoCodeData = MutableStateFlow<GeoCodeResponse?>(null)
    val geoCodeData: StateFlow<GeoCodeResponse?> = _geoCodeData

    fun isValidZip(zip: String): Boolean {
        return zip.matches(Regex("^\\d{5}$"))
    }

    fun searchWithGeoCode(zip: String) {
        viewModelScope.launch {
            try {
                val geoCodeResponse = airQualityRepository.getGeoCode(zip)
                _geoCodeData.value = geoCodeResponse
                geoCodeResponse?.let {
                    fetchAirQuality(it.lat, it.lon)
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error with GeoCode call: ", e)
            }
        }
    }

    private fun fetchAirQuality(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val airQualityResponse = airQualityRepository.getAirQuality(lat, lon)
                airQualityResponse?.let {
                // Extract overall AQI from the response
                val overallAqi = AQI.make(it.overallAqi)
                // Create a list of pollutants using the AQI data
                val pollutants = makePollutantList(airQualityResponse)

                _pollutantList.value = pollutants
                _aqiData.value = overallAqi
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error with AirQuality call: ", e)
            }
        }
    }

    private fun makePollutantList(response: AirQualityResponse): List<Pollutant> {
        return listOf(
            Pollutant(PollutantType.CO, response.co.concentration, name = "CO", subscript = ""),
            Pollutant(PollutantType.NO2, response.no2.concentration, name = "NO", subscript = "2"),
            Pollutant(PollutantType.O3, response.o3.concentration, name = "O", subscript = "3"),
            Pollutant(PollutantType.SO2, response.so2.concentration, name = "SO", subscript = "2"),
            Pollutant(PollutantType.PM25, response.pm25.concentration, name = "PM", subscript = "2.5"),
            Pollutant(PollutantType.PM10, response.pm10.concentration, name = "PM", subscript = "10")
        )
    }
    fun calculateAQIGaugeAngle(aqi: Int?): Float {
        var aqiArc: Float = 0f
        if (aqi != null && aqi >= 300 ) {
            aqiArc = (aqi - 300) * (45f/200) + 225
        } else if (aqi != null && aqi >= 200) {
            aqiArc = (aqi -200) * (45f/100) + 180
        } else if (aqi != null) {
            aqiArc = aqi * (180f/200)
        }
        return aqiArc
    }
}