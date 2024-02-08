package com.example.neptune.data.room.upvotes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neptune.data.room.streaming.StreamingConnectionData


/**
 * Room database class for managing upvote data.
 */
@Database(
    entities = [UpvoteData::class],
    version = 1,
    exportSchema = false
)
abstract class UpvoteDataDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO interface for upvote data operations.
     */
    abstract val upvoteDataDao: UpvoteDataDao

}