package com.example.neptune.data.model.streamingConnector.spotifyConnector

import com.example.neptune.data.model.streamingConnector.StreamingConnectionDatabase
import com.example.neptune.data.room.app.AppData
import com.example.neptune.data.room.streaming.StreamingConnectionData
import com.example.neptune.data.room.streaming.StreamingConnectionDataDao


class SpotifyConnectionDatabase(
    private val streamingConnectionDataDao: StreamingConnectionDataDao
) : StreamingConnectionDatabase {


    override suspend fun hasLinkedEntry(): Boolean {
        return streamingConnectionDataDao.entryCount() != 0
    }

    override suspend fun isLinked(): Boolean {
        if (hasLinkedEntry()) {
            return streamingConnectionDataDao.isLinked()
        } else {
            //TODO ist das sinnvoll???
            Exception("Linked is not set, but must be already set")
            return false
        }
    }

    override suspend fun setLinked(isLinked: Boolean) {
        if (!hasLinkedEntry()) {
            streamingConnectionDataDao.upsert(StreamingConnectionData(0, isLinked, ""))
        } else {
            streamingConnectionDataDao.setLinked(isLinked)
        }
    }

    override suspend fun getRefreshToken(): String {
        if (hasLinkedEntry()) {
            return streamingConnectionDataDao.getRefreshToken()
        } else {
            //TODO ist das sinnvoll???
            Exception("Linked is not set, but must be already set for getting a refresh token")
            return ""
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        if (hasLinkedEntry()) {
            return streamingConnectionDataDao.setRefreshToken(refreshToken)
        } else {
            //TODO ist das sinnvoll???
            Exception("Linked is not set, but must be already set for setting a refresh token")
        }
    }

}