package com.example.neptune.data.model.backendConnector

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.track.src.Track
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Timestamp

abstract class BackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) {

    protected val baseUrl = NeptuneApp.context.getString(R.string.backend_url)


    fun getUserSessionState(callback: (userSessionState: String) -> Unit) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)

        sendRequest("getUserSessionState", postData) { jsonResponse ->
            callbackUserSessionState(jsonResponse, callback)
        }
    }

    private fun callbackUserSessionState(
        jsonResponse: JSONObject,
        callback: (userSessionState: String) -> Unit
    ) {
        val userSessionState = jsonResponse.getString("userSessionState")
        callback(userSessionState)
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
                val isUpvoted = currentJsonTrack.getBoolean("isUpvoted")
                val colldownflag = currentJsonTrack.getBoolean("hasCooldown")

                val trackToAdd = Track(
                    trackId,
                    trackName,
                    artists,
                    genres,
                    imageUrl,
                    upvotes,
                    isUpvoted,
                    hasCooldown = colldownflag
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


    fun addTrackToSession(track: Track) {
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.id)
        postData.put("trackName", track.name)
        postData.put("artist", JSONArray().put("placeholder"))
        postData.put("genre", JSONArray())
        postData.put("imageURL", track.imageUrl)

        sendRequest("addTrackToSession", postData)
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
                Log.e("BACKEND VOLLEY", "Backend Server Request Error: ${error.localizedMessage}")
            })
        volleyQueue.add(jsonObjectRequest)
    }



}