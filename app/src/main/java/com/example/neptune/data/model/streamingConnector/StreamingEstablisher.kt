package com.example.neptune.data.model.streamingConnector

import androidx.compose.runtime.MutableState
import com.example.neptune.data.model.streamingConnector.spotifyConnector.StreamingLevel


interface StreamingEstablisher {


    suspend fun restoreConnectionIfPossible()

    fun getStreamingLevel(): MutableState<StreamingLevel>

    fun initiateConnectWithAuthorize()

    fun finishConnectWithCode(code: String)

    fun disconnect()

}