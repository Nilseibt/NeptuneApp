package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.track.Track
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.min


/**
 * Class representing the Spotify connector for the host device, providing functionalities for controlling playback.
 * @property volleyQueue The request queue for handling network requests.
 * @property accessToken The access token required for authorization with the Spotify API.
 * @property playbackState The current playback state.
 * @property trackIdWhichShouldBePlayed The ID of the track that should be played.
 */
class HostSpotifyConnector(
    private val volleyQueue: RequestQueue,
    private val accessToken: String
) : SpotifyConnector(volleyQueue, accessToken), HostStreamingConnector {


    private var playbackState = mutableStateOf(PlaybackState.INITIAL)

    private var trackIdWhichShouldBePlayed = ""


    /**
     * Plays the specified track from the given position.
     * @param track The track to be played.
     * @param positionMs The position in milliseconds from which to start playback.
     */
    override fun playTrack(track: Track, positionMs: Int) {
        playTrackById(track.id, positionMs)
    }

    private fun playTrackById(trackId: String, positionMs: Int, onCallback: () -> Unit = {}) {
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
                onCallback()
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


    /**
     * Sets the play progress of the currently playing track.
     * @param progress The progress value between 0 and 1.
     * @param onCallback The optional callback function to execute after setting the play progress.
     */
    override fun setPlayProgress(progress: Float, onCallback: () -> Unit) {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newGetRequest("https://api.spotify.com/v1/me/player", headers, parameters) { jsonResponse ->
            callbackPlayProgress(jsonResponse, progress, onCallback)
        }
    }

    private fun callbackPlayProgress(
        jsonResponse: JSONObject,
        progress: Float,
        onCallback: () -> Unit
    ) {
        val durationMs = jsonResponse.getJSONObject("item").getInt("duration_ms")
        val currentlyPlayingTrackId = jsonResponse.getJSONObject("item").getString("id")
        playTrackById(
            currentlyPlayingTrackId,
            ((durationMs.toFloat() - 5000f) * progress).toInt()
        ) {
            onCallback()
        }
    }

    /**
     * Adds the specified track to the streaming queue.
     * @param track The track to be added to the queue.
     * @param onCallback The optional callback function to execute after adding the track.
     */
    override fun addTrackToQueue(track: Track, onCallback: () -> Unit) {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()
        parameters["uri"] = "spotify%3Atrack%3A${track.id}"

        newRequest(
            "https://api.spotify.com/v1/me/player/queue",
            Request.Method.POST,
            headers,
            parameters,
            onCallback
        )
        playbackState.value = PlaybackState.PLAYING
        trackIdWhichShouldBePlayed = track.id
    }

    /**
     * Skips to the next track in the playback queue.
     */
    override fun skip() {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newRequest(
            "https://api.spotify.com/v1/me/player/next",
            Request.Method.POST,
            headers,
            parameters
        )
        playbackState.value = PlaybackState.PLAYING
    }

    /**
     * Refills the playback queue if needed and updates the player state.
     * @param onRefillQueue The callback function to execute if the queue needs to be refilled.
     * @param updatePlayProgress The callback function to update the play progress.
     */
    override fun getPlayerStateAndRefillQueueIfNeeded(
        onRefillQueue: () -> Unit,
        updatePlayProgress: (Float) -> Unit
    ) {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newGetRequest("https://api.spotify.com/v1/me/player", headers, parameters) { jsonResponse ->
            callbackPlayerStateRefillQueueIfNeeded(jsonResponse, onRefillQueue, updatePlayProgress)
        }
    }

    private fun callbackPlayerStateRefillQueueIfNeeded(
        jsonResponse: JSONObject,
        onRefillQueueNeeded: () -> Unit,
        updatePlayProgress: (Float) -> Unit
    ) {
        val isPlaying = jsonResponse.getBoolean("is_playing")
        if (isPlaying && playbackState.value == PlaybackState.PAUSED) {
            playbackState.value = PlaybackState.PLAYING
        }
        if (!isPlaying && playbackState.value == PlaybackState.PLAYING) {
            playbackState.value = PlaybackState.PAUSED
        }
        val progressMs = jsonResponse.getInt("progress_ms")
        val durationMs = jsonResponse.getJSONObject("item").getInt("duration_ms")
        val currentlyPlayingTrackId = jsonResponse.getJSONObject("item").getString("id")
        updatePlayProgress(min(1f, progressMs.toFloat() / (durationMs.toFloat() - 5000f)))
        if (durationMs - progressMs < 5000 && currentlyPlayingTrackId == trackIdWhichShouldBePlayed) {
            onRefillQueueNeeded()
        }
    }

    /**
     * Pauses the playback.
     */
    override fun pausePlay() {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newRequest(
            "https://api.spotify.com/v1/me/player/pause",
            Request.Method.PUT,
            headers,
            parameters
        )
        playbackState.value = PlaybackState.PAUSED
    }

    /**
     * Resumes the playback.
     */
    override fun resumePlay() {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newRequest(
            "https://api.spotify.com/v1/me/player/play",
            Request.Method.PUT,
            headers,
            parameters
        )
        playbackState.value = PlaybackState.PLAYING
    }

    /**
     * Retrieves the current playback state.
     * @return The playback state.
     */
    override fun getPlaybackState(): MutableState<PlaybackState> {
        return playbackState
    }

    /**
     * Sets the playback state to the specified state.
     * @param playbackState The new playback state.
     */
    override fun setPlaybackState(playbackState: PlaybackState) {
        this.playbackState.value = playbackState
    }

    /**
     * Checks if a player device is available for playback.
     * @param onDeviceAvailable The callback function to execute if a device is available.
     * @param onNoDeviceAvailable The callback function to execute if no device is available.
     */
    override fun checkIfPlayerDeviceAvailable(
        onDeviceAvailable: () -> Unit,
        onNoDeviceAvailable: () -> Unit
    ) {
        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()

        newGetRequest(
            "https://api.spotify.com/v1/me/player/devices",
            headers,
            parameters
        ) { jsonResponse ->
            callbackPlayerDevices(jsonResponse, onDeviceAvailable, onNoDeviceAvailable)
        }

    }

    private fun callbackPlayerDevices(
        jsonResponse: JSONObject,
        onDeviceAvailable: () -> Unit,
        onNoDeviceAvailable: () -> Unit
    ) {
        val devicesJsonArray = jsonResponse.getJSONArray("devices")
        if (devicesJsonArray.length() > 0) {
            onDeviceAvailable()
        } else {
            onNoDeviceAvailable()
        }
    }

}

