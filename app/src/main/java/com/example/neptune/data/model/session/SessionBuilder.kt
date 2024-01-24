package com.example.neptune.data.model.session

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.neptune.data.resources.GenreList
import kotlin.math.min

class SessionBuilder {

    private var sessionType: SessionType? = null

    private var selectedEntities = mutableStateListOf<String>() // artists or genres

    private var playlistLink = ""

    private var trackCooldown = -1


    fun setSessionType(sessionType: SessionType) {
        this.sessionType = sessionType
    }

    fun getSessionType(): SessionType {
        if (sessionType == null) {
            Exception("Session type must be set before getter is called")
        }
        return sessionType!!
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


    fun searchMatchingGenres(searchInput: String): SnapshotStateList<String> {
        if(searchInput == ""){
            return mutableStateListOf()
        }
        val matchingGenres = mutableListOf<String>()
        GenreList().getGenreList().forEach {
            if(it.lowercase().contains(searchInput)){
                matchingGenres.add(it)
            }
        }
        val sorter: (String) -> Int = { string -> string.length }
        matchingGenres.sortBy(sorter)
        return matchingGenres.subList(0, min(matchingGenres.size, 20)).toMutableStateList()
    }


}