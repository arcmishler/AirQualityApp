package com.arcmishler.airqualityapp.model

data class Pollutant(
    val name: String,
    val value: Double,
    val aqRange: PollutantLevels?
)

data class PollutantLevels (
    val good: IntRange,
    val fair: IntRange,
    val moderate: IntRange,
    val poor: IntRange,
    val veryPoor: IntRange,
    val hazardous: IntRange
)