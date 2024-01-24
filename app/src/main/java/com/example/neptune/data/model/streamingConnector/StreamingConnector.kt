package com.example.neptune.data.model.streamingConnector

import com.example.neptune.model.track.src.TrackList


interface StreamingConnector {

    fun search(searchInput: String): TrackList


}