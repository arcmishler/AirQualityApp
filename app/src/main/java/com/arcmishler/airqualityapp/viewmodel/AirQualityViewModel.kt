package com.arcmishler.airqualityapp.viewmodel

import androidx.lifecycle.ViewModel
import com.arcmishler.airqualityapp.model.AirQuality
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AirQualityViewModel : ViewModel() {
    private val apiKey = ""

    private val _airQualityData = MutableStateFlow<AirQuality?>(null)
    val airQualityData: StateFlow<AirQuality?> = _airQualityData
}