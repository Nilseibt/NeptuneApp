package com.example.neptune.data.room.streaming

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streaming_connection_data")
data class StreamingConnectionData(
    @ColumnInfo(name = "artificial_id")
    @PrimaryKey
    val artificialId: Int,
    @ColumnInfo(name = "is_linked")
    val isLinked: Boolean,
    @ColumnInfo(name = "refresh_token")
    val refreshToken: String
)