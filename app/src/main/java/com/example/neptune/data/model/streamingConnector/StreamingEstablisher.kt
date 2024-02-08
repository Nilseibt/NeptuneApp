package com.example.neptune.data.model.streamingConnector

import androidx.compose.runtime.MutableState
import com.example.neptune.data.model.streamingConnector.spotifyConnector.StreamingLevel
import com.example.neptune.data.model.track.Track


/**
 * Interface defining methods for establishing and managing the connection with a streaming service.
 */
interface StreamingEstablisher {

    /**
     * Retrieves the access token.
     * @return The access token.
     */
    fun getAccessToken(): String

    /**
     * Retrieves the refresh token.
     * @return The refresh token.
     */
    fun getRefreshToken(): String

    /**
     * Attempts to restore the connection if possible.
     * @param onRestoreFinished Callback invoked after attempting to restore the connection.
     */
    suspend fun restoreConnectionIfPossible(onRestoreFinished: () -> Unit)

    /**
     * Retrieves the streaming level.
     * @return The mutable state of the streaming level.
     */
    fun getStreamingLevel(): MutableState<StreamingLevel>

    /**
     * Initiates the connection process with the streaming service using the authorization flow.
     */
    fun initiateConnectWithAuthorize()

    /**
     * Completes the connection process with the streaming service using the provided authorization code.
     * @param code The authorization code.
     */
    fun finishConnectWithCode(code: String)

    /**
     * Disconnects from the streaming service.
     */
    fun disconnect()

    /**
     * Searches for artists matching the provided search input.
     * @param searchInput The search input to find matching artists.
     * @param callback The callback to handle the list of matching artists.
     */
    fun searchMatchingArtists(searchInput: String, callback: (List<String>) -> Unit)

    /**
     * Retrieves the playlist from the streaming service with the specified playlist ID.
     * @param playlistId The ID of the playlist to retrieve.
     * @param callback The callback to handle the retrieved playlist.
     */
    fun getPlaylist(playlistId: String, callback: (MutableList<Track>) -> Unit)

}