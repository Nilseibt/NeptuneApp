package com.example.neptune.data.room.streaming

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StreamingConnectionData::class],
    version = 1,
    exportSchema = false
)
abstract class StreamingConnectionDataDatabase : RoomDatabase() {

    abstract val streamingConnectionDataDao: StreamingConnectionDataDao

}