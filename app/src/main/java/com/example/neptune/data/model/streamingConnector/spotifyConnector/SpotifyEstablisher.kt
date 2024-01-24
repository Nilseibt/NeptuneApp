package com.example.neptune.data.model.streamingConnector.spotifyConnector

import androidx.compose.runtime.mutableStateOf
import com.android.volley.RequestQueue
import com.example.neptune.data.model.streamingConnector.StreamingConnectorEstablisher
import com.example.neptune.data.room.streaming.StreamingConnectionDataDao

class SpotifyEstablisher(
    private val spotifyConnectionDatabase: SpotifyConnectionDatabase,
    private val volleyQueue: RequestQueue
) : StreamingConnectorEstablisher {

    private var accessToken = ""
    private var refreshToken = ""

    private var spotifyLevel = mutableStateOf(SpotifyLevel.UNDETERMINED)

    override suspend fun restoreConnectionIfPossible() {
        if (spotifyConnectionDatabase.hasLinkedEntry()) {
            if (spotifyConnectionDatabase.isLinked()) {
                refreshToken = spotifyConnectionDatabase.getRefreshToken()
                if(refreshToken != ""){
                    connectWithRefreshToken()
                }else{
                    initiateConnectWithAuthorize()
                }
            }else{
                spotifyLevel.value = SpotifyLevel.UNLINKED
            }
        } else {
            spotifyConnectionDatabase.setLinked(false)
            spotifyLevel.value = SpotifyLevel.UNLINKED
        }
    }

    override fun initiateConnectWithAuthorize() {
        //TODO
    }

    override fun finishConnectWithCode(code: String) {
        //TODO
    }

    override fun disconnect() {
        //TODO
    }


    private fun connectWithRefreshToken() {
        //TODO
    }


}