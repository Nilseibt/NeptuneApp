package com.example.neptune.data.model.streamingConnector.spotifyConnector

import com.example.neptune.data.model.streamingConnector.StreamingConnectionDatabase
import com.example.neptune.data.room.app.AppData
import com.example.neptune.data.room.streaming.StreamingConnectionData
import com.example.neptune.data.room.streaming.StreamingConnectionDataDao


/**
 * SpotifyConnectionDatabase is a database wrapper responsible for managing the connection data related to Spotify streaming service.
 * It implements the StreamingConnectionDatabase interface.
 * @property streamingConnectionDataDao The DAO (Data Access Object) for accessing and managing streaming connection data in the database.
 */
class SpotifyConnectionDatabase(
    private val streamingConnectionDataDao: StreamingConnectionDataDao
) : StreamingConnectionDatabase {


    /**
     * Checks if there is at least one linked entry in the database.
     * @return True if there is at least one linked entry, false otherwise.
     */
    override suspend fun hasLinkedEntry(): Boolean {
        return streamingConnectionDataDao.entryCount() != 0
    }

    /**
     * Checks if the Spotify connection is linked.
     * @return True if the Spotify connection is linked, false otherwise.
     * @throws Exception if linked is not set but must be already set.
     */
    override suspend fun isLinked(): Boolean {
        if (hasLinkedEntry()) {
            return streamingConnectionDataDao.isLinked()
        } else {
            throw Exception("Linked is not set, but must be already set")
        }
    }

    /**
     * Sets the linked status of the Spotify connection.
     * @param isLinked Boolean indicating whether the connection is linked.
     */
    override suspend fun setLinked(isLinked: Boolean) {
        if (!hasLinkedEntry()) {
            streamingConnectionDataDao.upsert(StreamingConnectionData(0, isLinked, ""))
        } else {
            streamingConnectionDataDao.setLinked(isLinked)
        }
    }

    /**
     * Retrieves the refresh token used for refreshing Spotify access tokens.
     * @return The refresh token.
     * @throws Exception if linked is not set but must be already set for getting a refresh token.
     */
    override suspend fun getRefreshToken(): String {
        if (hasLinkedEntry()) {
            return streamingConnectionDataDao.getRefreshToken()
        } else {
            throw Exception("Linked is not set, but must be already set for getting a refresh token")
        }
    }

    /**
     * Sets the refresh token used for refreshing Spotify access tokens.
     * @param refreshToken The refresh token.
     * @throws Exception if linked is not set but must be already set for setting a refresh token.
     */
    override suspend fun setRefreshToken(refreshToken: String) {
        if (hasLinkedEntry()) {
            streamingConnectionDataDao.setRefreshToken(refreshToken)
        } else {
            throw Exception("Linked is not set, but must be already set for setting a refresh token")
        }
    }

}