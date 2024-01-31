package com.example.neptune.data.room.upvotes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neptune.data.room.streaming.StreamingConnectionData
import com.example.neptune.data.room.streaming.StreamingConnectionDataDao

@Database(
    entities = [StreamingConnectionData::class],
    version = 1,
    exportSchema = false
)
abstract class UpvoteDataDatabase : RoomDatabase() {

    abstract val streamingConnectionDataDao: StreamingConnectionDataDao

}