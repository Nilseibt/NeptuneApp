package com.example.neptune.data.room.app

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Represents the Room database for storing application data.
 */
@Database(
    entities = [AppData::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataDatabase : RoomDatabase() {

    /**
     * Provides access to the AppDataDao for performing database operations related to AppData entities.
     */
    abstract val appDataDao: AppDataDao

}