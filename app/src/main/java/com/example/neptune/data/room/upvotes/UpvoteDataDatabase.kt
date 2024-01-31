package com.example.neptune.data.room.upvotes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neptune.data.room.streaming.StreamingConnectionData

@Database(
    entities = [UpvoteData::class],
    version = 1,
    exportSchema = false
)
abstract class UpvoteDataDatabase : RoomDatabase() {

    abstract val upvoteDataDao: UpvoteDataDao

}