package com.example.neptune.data.room.spotify

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spotify_connection_data")
data class SpotifyConnectionData(
    @ColumnInfo(name = "artificial_id")
    @PrimaryKey
    val artificialId: Int,
    @ColumnInfo(name = "is_linked")
    val isLinked: Boolean,
    @ColumnInfo(name = "refresh_token")
    val refreshToken: String
)