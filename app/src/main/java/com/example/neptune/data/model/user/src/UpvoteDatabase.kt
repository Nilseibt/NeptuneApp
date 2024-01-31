package com.example.neptune.data.model.user.src

import com.example.neptune.data.model.session.Session
import com.example.neptune.data.room.upvotes.UpvoteData
import com.example.neptune.data.room.upvotes.UpvoteDataDao

class UpvoteDatabase(
    private val upvoteDataDao: UpvoteDataDao
) {

    suspend fun addUpvote(session: Session, trackId: String) {
        upvoteDataDao.upsert(UpvoteData(session.id, session.timestamp, trackId))
    }

    suspend fun removeUpvote(session: Session, trackId: String) {
        upvoteDataDao.delete(UpvoteData(session.id, session.timestamp, trackId))
    }

    suspend fun getStoredSessions(): List<Pair<Int, Int>> {
        return upvoteDataDao.getStoredSessions()
    }

    suspend fun getUpvotedTrackIds(session: Session): List<String> {
        return upvoteDataDao.getUpvotedTrackIds(session.id, session.timestamp)
    }


}