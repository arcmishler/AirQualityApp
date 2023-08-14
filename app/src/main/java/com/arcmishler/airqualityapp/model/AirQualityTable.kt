package com.arcmishler.airqualityapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_air_quality")
data class AirQualityTable(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "response") val responseJson: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)
