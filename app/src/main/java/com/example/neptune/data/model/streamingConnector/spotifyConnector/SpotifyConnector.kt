package com.example.neptune.data.model.streamingConnector.spotifyConnector

import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.model.track.src.TrackList

open class SpotifyConnector(authToken: String) : StreamingConnector {

    override fun search(searchInput: String): TrackList {
        //TODO
        return TrackList(mutableListOf())
    }


}