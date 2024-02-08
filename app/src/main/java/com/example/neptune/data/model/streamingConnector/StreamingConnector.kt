package com.example.neptune.data.model.streamingConnector

import com.example.neptune.data.model.track.Track


interface StreamingConnector {


    fun search(searchInput: String, resultLimit: Int, onCallbackFinished: (resultList: MutableList<Track>) -> Unit)

    fun searchWithGenres(searchInput: String, resultLimit: Int, onCallbackFinished: (resultList: MutableList<Track>) -> Unit)


}