package com.arcmishler.airqualityapp.model

class PollutantRanges {
    private val pollutantLevelsMap = mapOf(
        "SO2" to PollutantLevels(
            0..20,
            21..80,
            81..250,
            251..350,
            351..500,
            501..800
        ),
        "NO2" to PollutantLevels(
            0..40,
            41..70,
            71..150,
            151..200,
            201..350,
            351..800
        ),
        "PM10" to PollutantLevels(
            0..20,
            21..50,
            51..100,
            101..200,
            201..400,
            401..800
        ),
        "PM2_5" to PollutantLevels(
            0..10,
            11..25,
            26..50,
            51..75,
            76..100,
            101..800
        ),
        "O3" to PollutantLevels(
            0..60,
            61..100,
            101..140,
            141..180,
            180..250,
            251..800
        ),
        "CO" to PollutantLevels(
            0..4400,
            4401..9400,
            9401..12400,
            12401..15400,
            15401..20000,
            20001..50000
        )
    )

    fun getColorRange(component: String): PollutantLevels? {
        return pollutantLevelsMap[component]
    }
}