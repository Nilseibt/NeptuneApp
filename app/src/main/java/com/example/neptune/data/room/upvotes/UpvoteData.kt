package com.example.neptune.data.room.upvotes

import androidx.room.ColumnInfo
import androidx.room.Entity


/**
 * Represents an entity class for storing upvote data in the Room database.
 * @param sessionId The ID of the session associated with the upvote.
 * @param timestamp The timestamp of the upvote.
 * @param trackId The ID of the track that received the upvote.
 */
@Entity(tableName = "upvote_data", primaryKeys = ["session_id", "timestamp", "track_id"])
data class UpvoteData(
    @ColumnInfo(name = "session_id")
    val sessionId: Int,
    @ColumnInfo(name = "timestamp")
    val timestamp: Int,
    @ColumnInfo(name = "track_id")
    val trackId: String
)