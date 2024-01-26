package com.example.neptune.data.model.streamingConnector

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.neptune.data.model.streamingConnector.spotifyConnector.StreamingLevel


interface StreamingEstablisher {


    suspend fun restoreConnectionIfPossible()

    fun getStreamingLevel(): MutableState<StreamingLevel>

    fun initiateConnectWithAuthorize()

    fun finishConnectWithCode(code: String)

    fun disconnect()

    fun searchMatchingArtists(searchInput: String, callback: (List<String>) -> Unit)

}