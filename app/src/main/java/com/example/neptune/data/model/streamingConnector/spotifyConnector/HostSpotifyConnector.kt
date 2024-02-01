package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.track.src.PlayList
import com.example.neptune.data.model.track.src.Track
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import kotlin.math.min


class HostSpotifyConnector(
    private val volleyQueue: RequestQueue,
    private val accessToken: String,
    private val refreshToken: String
) : SpotifyConnector(volleyQueue, accessToken, refreshToken), HostStreamingConnector {


    private var playbackState = mutableStateOf(PlaybackState.INITIAL)

    private var trackIdWhichShouldBePlayed = ""


    override fun playTrack(track: Track, positionMs: Int) {
        playTrackById(track.id, positionMs)
    }

    private fun playTrackById(trackId: String, positionMs: Int){
        val url = "https://api.spotify.com/v1/me/player/play"
        val jsonData = JSONObject()
        jsonData.put("uris", JSONArray().put("spotify:track:${trackId}"))
        jsonData.put("position_ms", positionMs)

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.PUT, url, jsonData,
            { response ->
                Log.i(
                    "SPOTIFY JSON",
                    "$url $response"
                )
            },
            { error ->
                Log.e("SPOTIFY VOLLEY", "$url : Spotify Request Error: ${error}")
            }) {
            override fun getHeaders(): Map<String, String> {
                var headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $accessToken"
                return headers
            }
            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                if (response == null || response.data.isEmpty()) {
                    return Response.success(null, HttpHeaderParser.parseCacheHeaders(response))
                } else {
                    return super.parseNetworkResponse(response);
                }
            }
        }
        volleyQueue.add(jsonObjectRequest)
        playbackState.value = PlaybackState.PLAYING
        trackIdWhichShouldBePlayed = trackId
    }

    override fun setPlayProgress(progress: Float){
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newGetRequest("https://api.spotify.com/v1/me/player", headers, parameters){jsonResponse ->
            callbackPlayProgress(jsonResponse, progress)
        }
    }

    private fun callbackPlayProgress(jsonResponse: JSONObject, progress: Float){
        val durationMs = jsonResponse.getJSONObject("item").getInt("duration_ms")
        val currentlyPlayingTrackId = jsonResponse.getJSONObject("item").getString("id")
        playTrackById(currentlyPlayingTrackId, ((durationMs.toFloat() - 5000f)*progress).toInt())
    }

    override fun addTrackToStreamingQueue(track: Track, onCallback: () -> Unit) {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()
        parameters["uri"] = "spotify%3Atrack%3A${track.id}"

        newRequest("https://api.spotify.com/v1/me/player/queue", Request.Method.POST, headers, parameters, onCallback)
        playbackState.value = PlaybackState.PLAYING
        trackIdWhichShouldBePlayed = track.id
    }

    override fun skipTrack() {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newRequest("https://api.spotify.com/v1/me/player/next", Request.Method.POST, headers, parameters)
        playbackState.value = PlaybackState.PLAYING
    }

    override fun refillQueueIfNeeded(onRefillQueue: () -> Unit, updatePlayProgress: (Float) -> Unit) {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newGetRequest("https://api.spotify.com/v1/me/player", headers, parameters){jsonResponse ->
            callbackRefillQueueIfNeeded(jsonResponse, onRefillQueue, updatePlayProgress)
        }
    }

    private fun callbackRefillQueueIfNeeded(jsonResponse: JSONObject, onRefillQueueNeeded: () -> Unit, updatePlayProgress: (Float) -> Unit){
        val isPlaying = jsonResponse.getBoolean("is_playing")
        if(isPlaying) {
            val progressMs = jsonResponse.getInt("progress_ms")
            val durationMs = jsonResponse.getJSONObject("item").getInt("duration_ms")
            val currentlyPlayingTrackId = jsonResponse.getJSONObject("item").getString("id")
            updatePlayProgress(min(1f, progressMs.toFloat() / (durationMs.toFloat() - 5000f)))
            if(durationMs - progressMs < 5000 && currentlyPlayingTrackId == trackIdWhichShouldBePlayed){ //TODO make correct timing here
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

    override fun setPlaybackState(playbackState: PlaybackState) {
        this.playbackState.value = playbackState
    }

}

