package com.example.neptune.data.model.session

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.neptune.data.model.track.src.PlayList
import com.example.neptune.data.resources.GenreList
import java.sql.Timestamp
import kotlin.math.min

class SessionBuilder {

    private var sessionType = SessionType.GENERAL

    private var selectedEntities = mutableStateListOf<String>() // artists or genres

    private var playlistLink = ""

    private var trackCooldown = -1


    fun setSessionType(sessionType: SessionType) {
        this.sessionType = sessionType
    }

    fun getSessionType(): SessionType {
        return sessionType
    }

    fun isEntitySelected(entityName: String): Boolean {
        return selectedEntities.contains(entityName)
    }

    fun addEntity(entityName: String) {
        selectedEntities.add(entityName)
    }

    fun removeEntity(entityName: String) {
        selectedEntities.remove(entityName)
    }

    fun getSelectedEntities(): SnapshotStateList<String> {
        return selectedEntities
    }

    fun setPlaylistLink(playlistLink: String) {
        this.playlistLink = playlistLink
    }

    fun setTrackCooldown(trackCooldown: Int) {
        this.trackCooldown = trackCooldown
    }

    fun getTrackCooldown(): Int {
        return trackCooldown
    }


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

    // let some other method (maybe in the model) create the host backend connector and
    // make the backend call and then retrieve the full session provided the id and timestamp
    // from the backend
    fun createSession(sessionId: Int, sessionTimestamp: Int): Session {
        return when(sessionType){
            SessionType.GENERAL -> Session(sessionId, sessionTimestamp, trackCooldown)
            SessionType.ARTIST, SessionType.GENRE -> ArtistSession(sessionId, sessionTimestamp, trackCooldown, selectedEntities)
            SessionType.PLAYLIST -> PlaylistSession(sessionId, sessionTimestamp, trackCooldown, PlayList())
            //TODO make an actual playlist in the playlist session
        }
    }

    fun reset() {
        sessionType = SessionType.GENERAL
        selectedEntities = mutableStateListOf()
        playlistLink = ""
        trackCooldown = -1
    }


}