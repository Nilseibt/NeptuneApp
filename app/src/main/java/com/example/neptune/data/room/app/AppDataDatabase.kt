package com.example.neptune.data.room.app

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AppData::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataDatabase : RoomDatabase() {

    abstract val appDataDao: AppDataDao

}