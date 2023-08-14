package com.arcmishler.airqualityapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AirQualityDAO {
    @Query("SELECT * FROM cached_air_quality WHERE latitude = :latitude AND longitude = :longitude AND timestamp > :minTimestamp")
    suspend fun getCachedAirQuality(latitude: Double, longitude: Double, minTimestamp: Long): AirQualityTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirQuality(cachedAirQuality: AirQualityTable)
}