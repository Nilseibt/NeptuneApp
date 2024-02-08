package com.example.neptune.data.model.user

import com.example.neptune.data.model.session.Session
import com.example.neptune.data.room.upvotes.UpvoteData
import com.example.neptune.data.room.upvotes.UpvoteDataDao

class UpvoteDatabase(
    private val upvoteDataDao: UpvoteDataDao
) {

    suspend fun addUpvote(session: Session, trackId: String) {
        upvoteDataDao.upsert(UpvoteData(session.sessionId, session.timestamp, trackId))
    }

    suspend fun removeUpvote(session: Session, trackId: String) {
        upvoteDataDao.delete(UpvoteData(session.sessionId, session.timestamp, trackId))
    }

    suspend fun removeAllTracksWithSession(sessionId: Int, sessionTimestamp: Int){
        upvoteDataDao.deleteAllTracksWithSession(sessionId, sessionTimestamp)
    }

    suspend fun getStoredSessions(): List<Pair<Int, Int>> {
        val storedSessionsAsLists = upvoteDataDao.getStoredSessions()
        val storedSessionsAsPairs = mutableListOf<Pair<Int, Int>>()
        storedSessionsAsLists.forEach {
            storedSessionsAsPairs.add(Pair(it.session_id, it.timestamp))
        }
        return storedSessionsAsPairs
    }

    suspend fun getUpvotedTrackIds(session: Session): List<String> {
        return upvoteDataDao.getUpvotedTrackIds(session.sessionId, session.timestamp)
    }


}