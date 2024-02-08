package com.example.neptune.data.model.session

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.neptune.data.model.track.PlayList
import com.example.neptune.data.model.track.Track
import com.example.neptune.data.resources.GenreList
import kotlin.math.min


/**
 * Builder class for creating sessions with various configurations, all data is stored for the actual creation.
 */
class SessionBuilder {

    private var sessionType = SessionType.GENERAL

    private var selectedEntities = mutableStateListOf<String>() // artists or genres

    private var playlistTracks = mutableListOf<Track>()

    private var trackCooldown = 0


    /**
     * Sets the session type for the session being built.
     * @param sessionType The type of session.
     */
    fun setSessionType(sessionType: SessionType) {
        this.sessionType = sessionType
    }

    /**
     * Sets the session type based on the string received from the backend.
     * @param sessionTypeBackendString The session type string received from the backend.
     */
    fun setSessionTypeFromBackendString(sessionTypeBackendString: String) {
        sessionType = when (sessionTypeBackendString) {
            "General" -> SessionType.GENERAL
            "Artist" -> SessionType.ARTIST
            "Genre" -> SessionType.GENRE
            "Playlist" -> SessionType.PLAYLIST
            else -> SessionType.GENERAL //does not happen
        }
    }

    /**
     * Gets the session type of the session being built.
     * @return The session type.
     */
    fun getSessionType(): SessionType {
        return sessionType
    }

    /**
     * Converts the session type into a string compatible with the backend.
     * @return The session type string.
     */
    fun getSessionTypeAsBackendString(): String {
        return when (sessionType) {
            SessionType.GENERAL -> "General"
            SessionType.ARTIST -> "Artist"
            SessionType.GENRE -> "Genre"
            SessionType.PLAYLIST -> "Playlist"
        }
    }

    /**
     * Checks if a given entity (artist or genre) is selected for the session.
     * @param entityName The name of the entity to check.
     * @return True if the entity is selected, false otherwise.
     */
    fun isEntitySelected(entityName: String): Boolean {
        return selectedEntities.contains(entityName)
    }

    /**
     * Adds an entity (artist or genre) to the selected entities list for the session.
     * @param entityName The name of the entity to add.
     */
    fun addEntity(entityName: String) {
        selectedEntities.add(entityName)
    }

    /**
     * Removes an entity (artist or genre) from the selected entities list for the session.
     * @param entityName The name of the entity to remove.
     */
    fun removeEntity(entityName: String) {
        selectedEntities.remove(entityName)
    }

    /**
     * Gets the list of selected entities (artists or genres) for the session.
     * @return The list of selected entities.
     */
    fun getSelectedEntities(): SnapshotStateList<String> {
        return selectedEntities
    }

    /**
     * Sets the list of selected entities (artists or genres) for the session.
     * @param selectedEntities The list of selected entities.
     */
    fun setSelectedEntities(selectedEntities: List<String>) {
        this.selectedEntities = selectedEntities.toMutableStateList()
    }

    /**
     * Sets the list of playlist tracks for playlist sessions.
     * @param playlistTracks The list of playlist tracks.
     */
    fun setPlaylistTracks(playlistTracks: MutableList<Track>) {
        this.playlistTracks = playlistTracks
    }

    /**
     * Gets the list of playlist tracks for playlist sessions.
     * @return The list of playlist tracks.
     */
    fun getPlaylistTracks(): MutableList<Track> {
        return playlistTracks
    }

    /**
     * Sets the cooldown duration for tracks in the session.
     * @param trackCooldown The track cooldown duration.
     */
    fun setTrackCooldown(trackCooldown: Int) {
        this.trackCooldown = trackCooldown
    }

    /**
     * Gets the cooldown duration for tracks in the session.
     * @return The track cooldown duration.
     */
    fun getTrackCooldown(): Int {
        return trackCooldown
    }


    /**
     * Searches for matching genres (1500 hard coded genres available) based on a search input.
     * @param searchInput The search input for matching genres.
     * @return The list of matching genres.
     */
    fun searchMatchingGenres(searchInput: String): List<String> {
        if (searchInput == "") {
            return listOf()
        }
        val matchingGenres = mutableListOf<String>()
        GenreList().getGenreList().forEach {
            if (it.lowercase().contains(searchInput)) {
                matchingGenres.add(it)
            }
        }
        val sorter: (String) -> Int = { string -> string.length }
        matchingGenres.sortBy(sorter)
        return matchingGenres.subList(0, min(matchingGenres.size, 20))
    }


    /**
     * Creates a session based on the configured parameters.
     * @param sessionId The unique identifier for the session.
     * @param sessionTimestamp The timestamp for the session creation.
     * @return The created session.
     */
    fun createSession(sessionId: Int, sessionTimestamp: Int): Session {
        return when (sessionType) {
            SessionType.GENERAL -> Session(sessionId, sessionTimestamp, trackCooldown)
            SessionType.ARTIST -> ArtistSession(
                sessionId,
                sessionTimestamp,
                trackCooldown,
                selectedEntities
            )
            SessionType.GENRE -> GenreSession(
                sessionId,
                sessionTimestamp,
                trackCooldown,
                selectedEntities
            )
            SessionType.PLAYLIST -> PlaylistSession(
                sessionId, sessionTimestamp, trackCooldown,
            )
        }
    }

    /**
     * Resets the session builder to its initial empty state.
     */
    fun reset() {
        sessionType = SessionType.GENERAL
        selectedEntities = mutableStateListOf()
        playlistTracks = mutableListOf()
        trackCooldown = -1
    }


}