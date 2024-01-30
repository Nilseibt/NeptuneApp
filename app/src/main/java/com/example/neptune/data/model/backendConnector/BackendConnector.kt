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
                // TODO Problems: Timestamp, and parameter names of isUpvoted and hasCooldown might be different
                val isUpvoted = currentJsonTrack.getBoolean("isUpvoted")
                val hasCooldown = currentJsonTrack.getBoolean("hasCooldown")

                val trackToAdd = Track(
                    trackId,
                    trackName,
                    artists,
                    genres,
                    imageUrl,
                    Timestamp(0),
                    upvotes,
                    isUpvoted,
                    hasCooldown
                )
                listOfTracks.add(trackToAdd)
            }
        }
        callback(listOfTracks)
    }


    suspend fun getStatistics() {
        //TODO needs implementation
    }


    suspend fun isSessionOpen() {
        //TODO needs implementation
    }


    fun addTrackToSession(track: Track) {
        //TODO does not work yet
        return
        /*val url = baseUrl + "addTrackToSession"
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.spotifyId)
        postData.put("trackName", track.trackName)
        postData.put("artist", JSONArray().put("placeholder"))
        postData.put("genre", JSONArray())
        postData.put("imageURL", track.imageUrl)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            {response ->
                Log.i("JSON ADD", response.toString())
            },
            {error ->
                Log.e("VOLLEY", "Server Request Error: ${error.localizedMessage}")
            })

        volleyQueue.add(jsonObjectRequest)*/
    }


    fun addUpvoteToTrack(track: Track) {
        //TODO does not work yet
        return
        /*val url = baseUrl + "addUpvoteToTrack"
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.spotifyId)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            {response ->
                Log.i("JSON ADD UPVOTE", response.toString())
            },
            {error ->
                Log.e("VOLLEY", "Server Request Error: ${error.localizedMessage}")
            })

        volleyQueue.add(jsonObjectRequest)*/
    }


    fun removeUpvoteFromTrack(track: Track) {
        //TODO does not work yet
        return
        /*val url = baseUrl + "removeUpvoteFromTrack"
        val postData = JSONObject()
        postData.put("deviceID", deviceId)
        postData.put("trackID", track.spotifyId)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            {response ->
                Log.i("JSON REM UPVOTE", response.toString())
            },
            {error ->
                Log.e("VOLLEY", "Server Request Error: ${error.localizedMessage}")
            })

        volleyQueue.add(jsonObjectRequest)*/
    }


    suspend fun getLockedTracks() {
        //TODO needs implementation
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