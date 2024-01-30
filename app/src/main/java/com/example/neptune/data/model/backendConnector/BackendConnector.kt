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

abstract class BackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) {

    protected val baseUrl = NeptuneApp.context.getString(R.string.backend_url)


    fun getUserSessionState(callback: (String) -> Unit) {
        val url = baseUrl + "getUserSessionState"
        val postData = JSONObject()
        postData.put("deviceID", deviceId)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            {response ->
                Log.i("JSON STATE", response.toString())
                val userSessionState: String = response.getString("userSessionState")
                callback(userSessionState)
            },
            {error ->
                Log.e("VOLLEY", "Server Request Error: ${error.localizedMessage}")
            })

        volleyQueue.add(jsonObjectRequest)
    }


    fun getAllTrackData(callback: (List<Track>) -> Unit) {
        //TODO does not work yet
        return
        /*val url = baseUrl + "getAllTrackData"
        val postData = JSONObject()
        postData.put("deviceID", deviceId)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            {response ->
                Log.i("JSON TRACKS", response.toString())
                val tracks = mutableListOf<Track>()
                if(response.get("tracks").toString() != "null") {
                    val jsonTracksArray = response.getJSONArray("tracks")
                    for (trackIndex in 0 until jsonTracksArray.length()) {
                        val currentJsonTrack = jsonTracksArray.getJSONObject(trackIndex)
                        val trackId = currentJsonTrack.getString("trackID")
                        val trackName = currentJsonTrack.getString("trackName")
                        val imageUrl = currentJsonTrack.getString("imageURL")
                        val genres = mutableListOf("placeholder")
                        val artists = mutableListOf("placeholder")
                        val upvotes = currentJsonTrack.getInt("upvotes")
                        val trackToAdd = Track(trackId, trackName, imageUrl, genres, artists)
                        trackToAdd.upvoteCount = upvotes
                        tracks.add(trackToAdd)
                    }
                }
                callback(tracks)
            },
            {error ->
                Log.e("VOLLEY", "Server Request Error: ${error.localizedMessage}")
            })

        volleyQueue.add(jsonObjectRequest)*/
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



}