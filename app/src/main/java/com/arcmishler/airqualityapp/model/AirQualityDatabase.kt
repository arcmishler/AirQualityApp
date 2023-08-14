package com.arcmishler.airqualityapp.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AirQualityTable::class], version = 1)
abstract class AirQualityDatabase : RoomDatabase() {
    abstract fun airQualityDao(): AirQualityDAO
}
