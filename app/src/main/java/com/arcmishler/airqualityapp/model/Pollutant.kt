package com.arcmishler.airqualityapp.model

import androidx.compose.ui.graphics.Color
import com.arcmishler.airqualityapp.ui.theme.AirGreen
import com.arcmishler.airqualityapp.ui.theme.AirMaroon
import com.arcmishler.airqualityapp.ui.theme.AirOrange
import com.arcmishler.airqualityapp.ui.theme.AirPurple
import com.arcmishler.airqualityapp.ui.theme.AirRed
import com.arcmishler.airqualityapp.ui.theme.AirYellow

// Define the enum class to represent different pollutants
enum class PollutantType(val ranges: PollutantRanges) {
    SO2(PollutantRanges(0..20, 21..80, 81..250, 251..350, 351..500, 501..800)),
    NO2(PollutantRanges(0..40, 41..70, 71..150, 151..200, 201..350, 351..800)),
    PM10(PollutantRanges(0..20, 21..50, 51..100, 101..200, 201..400, 401..800)),
    PM25(PollutantRanges(0..10, 11..25, 26..50, 51..75, 76..100, 101..800)),
    O3(PollutantRanges(0..60, 61..100, 101..140, 141..180, 180..250, 251..800)),
    CO(PollutantRanges(0..4400, 4401..9400, 9401..12400, 12401..15400, 15401..20000, 20001..50000))
}

data class PollutantRanges (
    val good: IntRange,
    val fair: IntRange,
    val moderate: IntRange,
    val poor: IntRange,
    val veryPoor: IntRange,
    val hazardous: IntRange
)

data class Pollutant(
    val type: PollutantType,
    val value: Double,
    val name: String,
    val subscript: String
) {
    val color: Color
        get() = getAQIColor(type.ranges, value)
}

fun getAQIColor(levels: PollutantRanges, value: Double): Color {
    return when (value.toInt()) {
        in levels.good -> AirGreen
        in levels.fair -> AirYellow
        in levels.moderate -> AirOrange
        in levels.poor -> AirRed
        in levels.veryPoor -> AirPurple
        in levels.hazardous -> AirMaroon
        else -> Color.Black
    }
}
