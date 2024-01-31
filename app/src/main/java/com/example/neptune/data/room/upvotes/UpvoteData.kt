package com.example.neptune.data.room.upvotes

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "upvote_data")
data class UpvoteData(
    @ColumnInfo(name = "session_id")
    val sessionId: Int,
    @ColumnInfo(name = "timestamp")
    val timestamp: Int,
    @ColumnInfo(name = "track_id")
    val trackId: String
)