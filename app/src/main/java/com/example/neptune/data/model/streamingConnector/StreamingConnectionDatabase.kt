package com.example.neptune.data.model.streamingConnector


/**
 * StreamingConnectionDatabase interface defines methods for managing streaming connection data in the database.
 * This interface provides functionality related to checking link status, setting link status, and managing refresh tokens.
 */
interface StreamingConnectionDatabase {

    /**
     * Checks if there is at least one linked entry in the database.
     * @return True if there is at least one linked entry, false otherwise.
     */
    suspend fun hasLinkedEntry(): Boolean

    /**
     * Checks if the streaming connection is linked.
     * @return True if the streaming connection is linked, false otherwise.
     */
    suspend fun isLinked(): Boolean

    /**
     * Sets the linked status of the streaming connection.
     * @param isLinked Boolean indicating whether the connection is linked.
     */
    suspend fun setLinked(isLinked: Boolean)

    /**
     * Retrieves the refresh token used for refreshing access tokens.
     * @return The refresh token.
     */
    suspend fun getRefreshToken(): String

    /**
     * Sets the refresh token used for refreshing access tokens.
     * @param refreshToken The refresh token.
     */

    suspend fun setRefreshToken(refreshToken: String)

}