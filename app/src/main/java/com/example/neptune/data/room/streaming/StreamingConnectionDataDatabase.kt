package com.example.neptune.data.room.streaming

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Represents the Room database for storing streaming connection data.
 */
@Database(
    entities = [StreamingConnectionData::class],
    version = 1,
    exportSchema = false
)
abstract class StreamingConnectionDataDatabase : RoomDatabase() {

    /**
     * Provides access to the StreamingConnectionDataDao for performing database operations related to streaming connection data.
     */
    abstract val streamingConnectionDataDao: StreamingConnectionDataDao

}