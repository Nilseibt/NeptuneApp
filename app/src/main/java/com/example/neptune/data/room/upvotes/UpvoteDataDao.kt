package com.example.neptune.data.room.upvotes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert


/**
 * Data Access Object (DAO) for performing database operations related to upvote data.
 */
@Dao
interface UpvoteDataDao{

    /**
     * Inserts or updates the given upvote data in the database.
     */
    @Upsert
    suspend fun upsert(upvoteData: UpvoteData)

    /**
     * Deletes the specified upvote data from the database.
     */
    @Delete
    suspend fun delete(upvoteData: UpvoteData)

    /**
     * Deletes all tracks associated with the given session ID and timestamp from the database.
     */
    @Query("DELETE FROM UPVOTE_DATA WHERE session_id = :sessionId AND timestamp = :timestamp")
    suspend fun deleteAllTracksWithSession(sessionId: Int, timestamp: Int)

    /**
     * Checks if a track is upvoted in a session.
     */
    @Query("SELECT COUNT() FROM UPVOTE_DATA WHERE session_id = :sessionId AND timestamp = :timestamp AND track_id = :trackId")
    suspend fun isUpvotedInSession(sessionId: Int, timestamp: Int, trackId: String): Int

    /**
     * Retrieves distinct session IDs and timestamps stored in the database.
     */
    @Query("SELECT DISTINCT session_id, timestamp FROM UPVOTE_DATA")
    suspend fun getStoredSessions(): List<SessionTimestampPair>

    /**
     * Retrieves the track IDs that were upvoted in the specified session.
     */
    @Query("SELECT track_id FROM UPVOTE_DATA WHERE session_id = :sessionId AND timestamp = :timestamp")
    suspend fun getUpvotedTrackIds(sessionId: Int, timestamp: Int): List<String>

    /**
     * Represents a pair of session ID and timestamp.
     */
    data class SessionTimestampPair(
        val session_id: Int,
        val timestamp: Int
    )
}