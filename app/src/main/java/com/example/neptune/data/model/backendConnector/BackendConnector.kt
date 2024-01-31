package com.example.neptune.data.model.backendConnector

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.track.src.VoteList
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Timestamp

open class BackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) {

    protected val baseUrl = NeptuneApp.context.getString(R.string.backend_url)


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
                    artists.add(jsonGenresArray.getString(genreIndex))
                }
            }
            callback(userSessionState, sessionId, timestamp, mode, artists, genres)
        }else {
            callback(userSessionState, -1, -1, "", listOf(), listOf())
        }
    }


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
                val artists = mutableListOf("placeholder")
                val upvotes = currentJsonTrack.getInt("upvotes")
                // TODO Problems: parameter names of isUpvoted and hasCooldown might be different
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


    fun getStatistics(
        callback: (
            mostUpvotedSong: String,
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
            mostUpvotedSong: String,
            mostUpvotedGenre: String,
            mostUpvotedArtist: String,
            totalPlayedTracks: Int,
            sessionDuration: String,
            totalParticipants: Int,
            totalUpvotes: Int
        ) -> Unit
    ) {
        callback(
            jsonResponse.getString("mostUpvotedSong"),
            jsonResponse.getString("mostUpvotedGenre"),
            jsonResponse.getString("mostUpvotedArtist"),
            jsonResponse.getInt("totalPlayedTracks"),
            jsonResponse.getString("sessionDuration"),
            jsonResponse.getInt("totalParticipants"),
            jsonResponse.getInt("totalUpvotes")
        )
    }


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


    fun addTrackToSession(track: Track, callback: () -> Unit) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.id)
        postData.put("trackName", track.name)
        postData.put("artist", JSONArray().put("placeholder"))
        postData.put("genre", JSONArray())
        postData.put("imageURL", track.imageUrl)

        sendRequest("addTrackToSession", postData) { callback() }
    }


    fun addUpvoteToTrack(track: Track) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.id)

        sendRequest("addUpvoteToTrack", postData)
    }


    fun removeUpvoteFromTrack(track: Track) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.id)

        sendRequest("removeUpvoteFromTrack", postData)
    }


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