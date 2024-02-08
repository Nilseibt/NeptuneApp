package com.example.neptune.data.model.user

import com.example.neptune.data.model.session.Session
import com.example.neptune.data.room.upvotes.UpvoteData
import com.example.neptune.data.room.upvotes.UpvoteDataDao

/**
 * Acts as a upvote database wrapper and manages upvote data in the local room database.
 * @property upvoteDataDao The Data Access Object (DAO) for accessing upvote data.
 */
class UpvoteDatabase(
    private val upvoteDataDao: UpvoteDataDao
) {

    /**
     * Adds an upvote for the specified track in the given session.
     * @param session The session associated with the upvote.
     * @param trackId The ID of the track to be upvoted.
     */
    suspend fun addUpvote(session: Session, trackId: String) {
        upvoteDataDao.upsert(UpvoteData(session.sessionId, session.timestamp, trackId))
    }

    /**
     * Removes an upvote for the specified track in the given session.
     * @param session The session associated with the upvote.
     * @param trackId The ID of the track to be un-upvoted.
     */
    suspend fun removeUpvote(session: Session, trackId: String) {
        upvoteDataDao.delete(UpvoteData(session.sessionId, session.timestamp, trackId))
    }

    /**
     * Removes all tracks associated with the specified session.
     * @param sessionId The ID of the session.
     * @param sessionTimestamp The timestamp of the session.
     */
    suspend fun removeAllTracksWithSession(sessionId: Int, sessionTimestamp: Int){
        upvoteDataDao.deleteAllTracksWithSession(sessionId, sessionTimestamp)
    }

    /**
     * Retrieves stored sessions from the database.
     * @return A list of session IDs and timestamps.
     */
    suspend fun getStoredSessions(): List<Pair<Int, Int>> {
        val storedSessionsAsLists = upvoteDataDao.getStoredSessions()
        val storedSessionsAsPairs = mutableListOf<Pair<Int, Int>>()
        storedSessionsAsLists.forEach {
            storedSessionsAsPairs.add(Pair(it.session_id, it.timestamp))
        }
        return storedSessionsAsPairs
    }

    /**
     * Retrieves the IDs of tracks that have been upvoted in the specified session.
     * @param session The session associated with the upvoted tracks.
     * @return A list of track IDs that have been upvoted.
     */
    suspend fun getUpvotedTrackIds(session: Session): List<String> {
        return upvoteDataDao.getUpvotedTrackIds(session.sessionId, session.timestamp)
    }


}