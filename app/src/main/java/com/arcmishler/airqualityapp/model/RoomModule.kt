package com.arcmishler.airqualityapp.model

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AirQualityDatabase {
        return Room.databaseBuilder(
            application,
            AirQualityDatabase::class.java,
            "air_quality_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAirQualityDao(database: AirQualityDatabase): AirQualityDAO {
        return database.airQualityDao()
    }
}
