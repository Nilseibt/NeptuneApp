package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.android.volley.RequestQueue
import com.example.neptune.data.model.streamingConnector.StreamingEstablisher

class SpotifyEstablisher(
    private val spotifyConnectionDatabase: SpotifyConnectionDatabase,
    private val volleyQueue: RequestQueue,
    private val context: Context
) : StreamingEstablisher {

    private var accessToken = ""
    private var refreshToken = ""

    private var spotifyLevel = mutableStateOf(StreamingLevel.UNDETERMINED)

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
                spotifyLevel.value = StreamingLevel.UNLINKED
            }
        } else {
            spotifyConnectionDatabase.setLinked(false)
            spotifyLevel.value = StreamingLevel.UNLINKED
        }
    }

    override fun getStreamingLevel(): MutableState<StreamingLevel> {
        return spotifyLevel
    }

    override fun initiateConnectWithAuthorize() {
        //TODO
        /*val spotifyClientId = context.getString(R.string.spotify_client_id)
        val url = "https://accounts.spotify.com/authorize?client_id=" +
                spotifyClientId +
                "&response_type=code&redirect_uri=" +
                "oauth://neptune-spotify-callback" +
                "&scope=user-modify-playback-state user-read-private"
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(context, Uri.parse(url))*/
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