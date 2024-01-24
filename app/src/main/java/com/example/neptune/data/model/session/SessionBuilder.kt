package com.example.neptune.data.model.session

import androidx.compose.runtime.mutableStateListOf

class SessionBuilder {

    private var sessionType: SessionType? = null

    private var artists = mutableStateListOf<String>()
    private var genres = mutableStateListOf<String>()

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
        if(sessionType == SessionType.ARTIST) {
            return artists.contains(entityName)
        }
        else {
            return genres.contains(entityName)
        }
    }

    fun addEntity(entityName: String) {
        if(sessionType == SessionType.ARTIST) {
            artists.add(entityName)
        }
        else {
            genres.add(entityName)
        }
    }

    fun removeEntity(entityName: String) {
        if(sessionType == SessionType.ARTIST) {
            artists.remove(entityName)
        }
        else {
            genres.remove(entityName)
        }
    }

    fun setPlaylistLink(playlistLink: String) {
        this.playlistLink = playlistLink
    }

    fun setTrackCooldown(trackCooldown: Int) {
        this.trackCooldown = trackCooldown
    }


}