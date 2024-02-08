package com.example.neptune.data.room.streaming

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents an entity class for storing streaming connection data in the Room database.
 * @param artificialId The artificial ID of the streaming connection data.
 * @param isLinked Indicates whether the streaming service is linked.
 * @param refreshToken The refresh token associated with the streaming service.
 */
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