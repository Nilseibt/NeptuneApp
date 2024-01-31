package com.example.neptune.data.room.upvotes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface UpvoteDataDao{
    @Upsert
    suspend fun upsert(upvoteData: UpvoteData)

    @Delete
    suspend fun delete(upvoteData: UpvoteData)

    @Query("SELECT COUNT() FROM UPVOTE_DATA WHERE session_id = :sessionId AND timestamp = :timestamp AND track_id = :trackId")
    suspend fun isUpvotedInSession(sessionId: Int, timestamp: Int, trackId: String): Int

    @Query("SELECT DISTINCT session_id, timestamp FROM UPVOTE_DATA")
    suspend fun getStoredSessions(): List<Pair<Int, Int>>

    @Query("SELECT track_id FROM UPVOTE_DATA WHERE session_id = :sessionId AND timestamp = :timestamp")
    suspend fun getUpvotedTrackIds(sessionId: Int, timestamp: Int): List<String>
}