package com.example.neptune.data.room.spotify

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SpotifyConnectionData::class],
    version = 1,
    exportSchema = false
)
abstract class SpotifyConnectionDataDatabase : RoomDatabase() {

    abstract val spotifyConnectionDataDao: SpotifyConnectionDataDao

}