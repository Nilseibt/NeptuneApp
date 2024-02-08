package com.example.neptune.data.model.streamingConnector

import com.example.neptune.data.model.track.Track


/**
 * Interface defining methods for searching tracks and searching tracks with genres.
 */
interface StreamingConnector {


    /**
     * Searches for tracks based on the provided search input.
     * @param searchInput The search query input.
     * @param resultLimit The maximum number of results to return.
     * @param onCallbackFinished The callback function to handle the search results.
     */
    fun search(searchInput: String, resultLimit: Int, onCallbackFinished: (resultList: MutableList<Track>) -> Unit)

    /**
     * Searches for tracks with genres based on the provided search input.
     * @param searchInput The search query input.
     * @param resultLimit The maximum number of results to return.
     * @param onCallbackFinished The callback function to handle the search results.
     */
    fun searchWithGenres(searchInput: String, resultLimit: Int, onCallbackFinished: (resultList: MutableList<Track>) -> Unit)


}