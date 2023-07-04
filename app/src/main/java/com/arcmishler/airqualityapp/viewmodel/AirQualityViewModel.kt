package com.arcmishler.airqualityapp.viewmodel

import androidx.lifecycle.ViewModel
import com.arcmishler.airqualityapp.api.AirPollutionAPIService
import com.arcmishler.airqualityapp.model.AirQuality
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val airPollutionApiService: AirPollutionAPIService
) : ViewModel() {
    private val apiKey = ""

    private val _airQualityData = MutableStateFlow<AirQuality?>(null)
    val airQualityData: StateFlow<AirQuality?> = _airQualityData
}