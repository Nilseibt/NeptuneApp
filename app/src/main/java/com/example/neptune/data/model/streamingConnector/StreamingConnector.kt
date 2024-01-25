package com.example.neptune.data.model.streamingConnector

import com.example.neptune.data.model.track.src.TrackList


interface StreamingConnector {

    fun search(searchInput: String): TrackList


}