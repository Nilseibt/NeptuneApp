package com.example.neptune.data.model.backendConnector

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.track.Track
import org.json.JSONArray
import org.json.JSONObject

/**
 * Base class for interacting with the backend server to retrieve and manipulate data.
 * @param deviceId The unique identifier of this device.
 * @param volleyQueue The Volley request queue for network communication.
 */
open class BackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) {

    /**
     * The base URL of the backend server.
     */
    protected val baseUrl = NeptuneApp.context.getString(R.string.backend_url)


    /**
     * Retrieves the current session state of the user.
     * @param callback Callback function to handle the response containing session state information.
     */
    fun getUserSessionState(
        callback: (
            userSessionState: String, sessionId: Int,
            timestamp: Int, mode: String, artists: List<String>,
            genres: List<String>
        ) -> Unit
    ) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)

        sendRequest("getUserSessionState", postData) { jsonResponse ->
            callbackUserSessionState(jsonResponse, callback)
        }
    }

    private fun callbackUserSessionState(
        jsonResponse: JSONObject,
        callback: (
            userSessionState: String, sessionId: Int, timestamp: Int, mode: String,
            artists: List<String>, genres: List<String>
        ) -> Unit
    ) {
        val userSessionState = jsonResponse.getString("userSessionState")
        if (userSessionState != "NONE") {
            val sessionId = jsonResponse.getInt("sessionID")
            val timestamp = jsonResponse.getInt("timestamp")
            val mode = jsonResponse.getString("modus")
            val artists = mutableListOf<String>()
            if (jsonResponse.get("artists").toString() != "null") {
                val jsonArtistsArray = jsonResponse.getJSONArray("artists")
                for (artistIndex in 0 until jsonArtistsArray.length()) {
                    artists.add(jsonArtistsArray.getString(artistIndex))
                }
            }
            val genres = mutableListOf<String>()
            if (jsonResponse.get("genres").toString() != "null") {
                val jsonGenresArray = jsonResponse.getJSONArray("genres")
                for (genreIndex in 0 until jsonGenresArray.length()) {
                    genres.add(jsonGenresArray.getString(genreIndex))
                }
            }
            callback(userSessionState, sessionId, timestamp, mode, artists, genres)
        } else {
            callback(userSessionState, -1, -1, "", listOf(), listOf())
        }
    }


    /**
     * Retrieves all track data from the backend server.
     * @param callback Callback function to handle the response containing all tracks of the session.
     */
    fun getAllTrackData(callback: (listOfTracks: List<Track>) -> Unit) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)

        sendRequest("getAllTrackData", postData) { jsonResponse ->
            callbackAllTrackData(jsonResponse, callback)
        }
    }

    private fun callbackAllTrackData(
        jsonResponse: JSONObject,
        callback: (listOfTracks: List<Track>) -> Unit
    ) {
        val listOfTracks = mutableListOf<Track>()
        if (jsonResponse.get("tracks").toString() != "null") {
            val jsonTracksArray = jsonResponse.getJSONArray("tracks")
            for (trackIndex in 0 until jsonTracksArray.length()) {
                val currentJsonTrack = jsonTracksArray.getJSONObject(trackIndex)
                val trackId = currentJsonTrack.getString("trackID")
                val trackName = currentJsonTrack.getString("trackName")
                val imageUrl = currentJsonTrack.getString("imageURL")
                val genres = mutableListOf("placeholder")
                val jsonArtistsArray = currentJsonTrack.getJSONArray("artists")
                val artists = mutableListOf <String>()
                for (artistIndex in 0 until jsonArtistsArray.length()) {
                    artists.add(jsonArtistsArray.getString(artistIndex))
                }
                val upvotes = currentJsonTrack.getInt("upvotes")
                val isUpvoted = false
                val isBlocked = currentJsonTrack.getInt("isBlocked") != 0
                val hasCooldown = currentJsonTrack.getInt("onCooldown") != 0


                val trackToAdd = Track(
                    trackId,
                    trackName,
                    artists,
                    genres,
                    imageUrl,
                    mutableIntStateOf(upvotes),
                    mutableStateOf(isUpvoted),
                    mutableStateOf(isBlocked),
                    mutableStateOf(hasCooldown)
                )
                listOfTracks.add(trackToAdd)
            }
        }
        callback(listOfTracks)
    }


    /**
     * Retrieves statistical information from the backend server.
     * @param callback Callback function to handle the response containing the statistical data.
     */
    fun requestStatistics(
        callback: (
            mostUpvotedTrack: String,
            mostUpvotedGenre: String,
            mostUpvotedArtist: String,
            totalPlayedTracks: Int,
            sessionDuration: String,
            totalParticipants: Int,
            totalUpvotes: Int
        ) -> Unit
    ) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)

        sendRequest("getStatistics", postData) { jsonResponse ->
            callbackStatistics(jsonResponse, callback)
        }
    }

    private fun callbackStatistics(
        jsonResponse: JSONObject, callback: (
            mostUpvotedTrack: String,
            mostUpvotedGenre: String,
            mostUpvotedArtist: String,
            totalPlayedTracks: Int,
            sessionDuration: String,
            totalParticipants: Int,
            totalUpvotes: Int
        ) -> Unit
    ) {
        var mostUpvotedTrack = ""
        if (jsonResponse.get("mostUpvotedSong").toString() != "null") {
            mostUpvotedTrack = jsonResponse.getString("mostUpvotedSong")
        }
        var mostUpvotedGenre = ""
        if (jsonResponse.get("mostUpvotedGenre").toString() != "null") {
            mostUpvotedGenre = jsonResponse.getString("mostUpvotedGenre")
        }
        var mostUpvotedArtist = ""
        if (jsonResponse.get("mostUpvotedArtist").toString() != "null") {
            mostUpvotedArtist = jsonResponse.getString("mostUpvotedArtist")
        }
        var totalPlayedTracks = 0
        if (jsonResponse.get("totalPlayedTracks").toString() != "null") {
            totalPlayedTracks = jsonResponse.getInt("totalPlayedTracks")
        }
        var sessionDuration = ""
        if (jsonResponse.get("sessionDuration").toString() != "null") {
            sessionDuration = jsonResponse.getString("sessionDuration")
        }
        var totalParticipants = 0
        if (jsonResponse.get("totalParticipants").toString() != "null") {
            totalParticipants = jsonResponse.getInt("totalParticipants") + 1
        }
        var totalUpvotes = 0
        if (jsonResponse.get("totalUpvotes").toString() != "null") {
            totalUpvotes = jsonResponse.getInt("totalUpvotes")
        }
        callback(
            mostUpvotedTrack,
            mostUpvotedGenre,
            mostUpvotedArtist,
            totalPlayedTracks,
            sessionDuration,
            totalParticipants,
            totalUpvotes
        )
    }


    /**
     * Checks if a session is open based on the provided session ID and timestamp.
     * @param sessionId The ID of the session to check.
     * @param sessionTimestamp The timestamp of the session to check.
     * @param callback Callback function to handle the response indicating whether the session is open.
     */
    fun isSessionOpen(sessionId: Int, sessionTimestamp: Int, callback: (isOpen: Boolean) -> Unit) {
        val postData = JSONObject()
        postData.put("sessionID", sessionId)
        postData.put("timestamp", sessionTimestamp)

        sendRequest("isSessionOpen", postData) { jsonResponse ->
            callbackSessionOpen(jsonResponse, callback)
        }
    }

    private fun callbackSessionOpen(jsonResponse: JSONObject, callback: (isOpen: Boolean) -> Unit) {
        val isOpen = jsonResponse.getBoolean("isOpen")
        callback(isOpen)
    }


    /**
     * Adds a track to the current session.
     * @param track The track to add.
     * @param callback Callback function to execute after adding the track successfully.
     */
    fun addTrackToSession(track: Track, callback: () -> Unit = {}) {

        val artistsJSONArray = JSONArray()
        track.artists.forEach {
            artistsJSONArray.put(it)
        }
        val genreJSONArray = JSONArray()
        track.genres.forEach {
            genreJSONArray.put(it)
        }

        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.id)
        postData.put("trackName", track.name)
        postData.put("artist", artistsJSONArray)
        postData.put("genre", genreJSONArray)
        postData.put("imageURL", track.imageUrl)

        sendRequest("addTrackToSession", postData) { callback() }
    }


    /**
     * Adds an upvote to a track.
     * @param track The track to upvote (the id is the unique identifier, other attributes do not matter)
     */
    fun addUpvoteToTrack(track: Track) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.id)

        sendRequest("addUpvoteToTrack", postData)
    }


    /**
     * Removes an upvote from a track.
     * @param track The track to remove the upvote from (the id is the unique identifier, other attributes do not matter)
     */
    fun removeUpvoteFromTrack(track: Track) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.id)

        sendRequest("removeUpvoteFromTrack", postData)
    }


    /**
     * Sends a request to the backend server with the specified URL path and data.
     * @param urlPath The path to append to the base URL to form the complete request URL.
     * @param postData The data to send with the request, as a json object.
     * @param callback Callback function to handle the successful response from the server.
     */
    protected fun sendRequest(
        urlPath: String,
        postData: JSONObject,
        callback: (jsonResponse: JSONObject) -> Unit = {}
    ) {
        val url = baseUrl + urlPath
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            { response ->
                Log.i("BACKEND JSON", "$urlPath $response")
                callback(response)
            },
            { error ->
                Log.e("BACKEND VOLLEY", "$urlPath : Backend Server Request Error: $error")
            })
        volleyQueue.add(jsonObjectRequest)
    }


}