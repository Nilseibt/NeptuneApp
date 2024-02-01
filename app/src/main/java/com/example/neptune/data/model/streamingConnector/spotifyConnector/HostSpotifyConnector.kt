package com.example.neptune.data.model.streamingConnector.spotifyConnector

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.track.src.PlayList
import com.example.neptune.data.model.track.src.Track
import org.json.JSONObject
import java.net.URLEncoder


class HostSpotifyConnector(
    private val volleyQueue: RequestQueue,
    private val accessToken: String,
    private val refreshToken: String
) : SpotifyConnector(volleyQueue, accessToken, refreshToken), HostStreamingConnector {


    private var playbackState = mutableStateOf(PlaybackState.INITIAL)

    override fun addTrackToStreamingQueue(track: Track) {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()
        parameters["uri"] = "spotify%3Atrack%3A${track.id}"

        newRequest("https://api.spotify.com/v1/me/player/queue", Request.Method.POST, headers, parameters)
        playbackState.value = PlaybackState.PLAYING
    }

    override fun skipTrack() {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newRequest("https://api.spotify.com/v1/me/player/next", Request.Method.POST, headers, parameters)
        playbackState.value = PlaybackState.PLAYING
    }

    override fun refillQueueIfNeeded(onRefillQueue: () -> Unit) {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newGetRequest("https://api.spotify.com/v1/me/player", headers, parameters){jsonResponse ->
            callbackRefillQueueIfNeeded(jsonResponse, onRefillQueue)
        }
    }

    private fun callbackRefillQueueIfNeeded(jsonResponse: JSONObject, onRefillQueueNeeded: () -> Unit){
        val isPlaying = jsonResponse.getBoolean("is_playing")
        if(isPlaying) {
            val progressMs = jsonResponse.getInt("progress_ms")
            val durationMs = jsonResponse.getJSONObject("item").getInt("duration_ms")
            if(durationMs - progressMs < 5000){ //TODO make correct timing here
                onRefillQueueNeeded()
            }
        }

    }

    override fun pausePlay() {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newRequest("https://api.spotify.com/v1/me/player/pause", Request.Method.PUT, headers, parameters)
        playbackState.value = PlaybackState.PAUSED
    }

    override fun resumePlay() {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newRequest("https://api.spotify.com/v1/me/player/play", Request.Method.PUT, headers, parameters)
        playbackState.value = PlaybackState.PLAYING
    }

    override fun setPlayProgress(percentage: Int) {
        //TODO
    }

    override fun isPlaylistLinkValid(): Boolean {
        //TODO
        return true
    }

    override fun getPlaylist(link: String): PlayList {
        //TODO
        return PlayList(mutableStateListOf())
    }

    override fun getPlaybackState(): MutableState<PlaybackState> {
        return playbackState
    }

}

