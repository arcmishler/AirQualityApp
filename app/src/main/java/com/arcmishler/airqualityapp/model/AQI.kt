package com.arcmishler.airqualityapp.model

import androidx.compose.ui.graphics.Color
import com.arcmishler.airqualityapp.R
import com.arcmishler.airqualityapp.ui.theme.*

data class AQI (
    val value: Int,
    val details: AQIDetails
)  {
    companion object {
        private val detailsMap = mapOf(
            0..50 to AQIDetails(
                AirGreen, R.drawable.green_aqi_4,
                "Good",
                "Air quality is satisfactory and poses little or no risk.\n" +
                        "You can enjoy your usual outdoor activities. " +
                        "You may choose to open your windows and ventilate your home to bring in outdoor air. "
            ),

            51..100 to AQIDetails(
                AirYellow, R.drawable.yellow_aqi_4,
                "Moderate",
                "Sensitive individuals should avoid outdoor activity as they may experience respiratory symptoms.\n" +
                        "Avoid ventilating indoor spaces with outdoor air, and close windows to avoid letting outdoor air pollution indoors.\n" +
                        "Note that sensitive groups for all categories include children, the elderly, pregnant people, and people with cardiac and pulmonary diseases. "
            ),

            101..150 to AQIDetails(
                AirOrange, R.drawable.orange_5,
                "Unhealthy for sensitive groups",
                "General public and sensitive individuals in particular are at risk to experience irritation and respiratory problems.\n" +
                        "When air quality is unhealthy for sensitive groups, everyone is at risk for eye, skin, and throat irritation as well as respiratory problems. " +
                        "The public should greatly reduce outdoor exertion.\n" +
                        "Sensitive groups are at greater health risk, should avoid all outdoor activity, and should consider wearing an air pollution mask outdoors. " +
                        "Ventilation is discouraged. A high-performance air purifier should be turned on if indoor air quality is unhealthy."
            ),

            151..200 to AQIDetails(
                AirRed, R.drawable.red_aqi_4,
                "Unhealthy",
                "Increased likelihood of adverse effects and aggravation to the heart and lungs among general public.\n" +
                        "Everyone should avoid and wear a pollution mask outdoors.\n" +
                        "Ventilation is discouraged. Air purifiers should be turned on."
            ),

            201..300 to AQIDetails(
                AirPurple, R.drawable.purple_aqi_4,
                "Very unhealthy",
                "General public will be noticeably affected. " +
                        "Sensitive groups should restrict outdoor activities.\n" +
                        "Everyone should avoid outdoor exercise and wear a pollution mask outdoors.\n" +
                        "Ventilation is discouraged. Air purifiers should be turned on."
            ),

            301..500 to AQIDetails(
                AirMaroon, R.drawable.maroon_4,
                "Hazardous",
                "General public at high risk of experiencing strong irritations and adverse health effects. " +
                        "Should avoid outdoor activities.\n" +
                        "Avoid exercise and remain indoors. Avoid outdoor exercise and wear a pollution mask outdoors.\n" +
                        "Ventilation is discouraged. Air purifiers should be turned on."
            ),
        )

        fun make(aqiValue: Int): AQI {
            val aqiDetails = detailsMap.entries.find { aqiValue in it.key }?.value
                ?: AQIDetails(
                    Color.Black,
                    R.drawable.green_aqi_4,
                    "Unknown",
                    "No information available."
                )
            return AQI(aqiValue, aqiDetails)
        }
    }
}

data class AQIDetails (
    val color: Color,
    val image: Int,
    val description: String,
    val detail1: String
)

