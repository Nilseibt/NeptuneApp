package com.example.neptune.data.model.backendConnector

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class ParticipantBackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) : BackendConnector(deviceId, volleyQueue) {


    fun participantJoinSession(
        sessionId: Int,
        callback: (timestamp: Int, mode: String, artists: List<String>, genres: List<String>) -> Unit
    ) {
        val postData = JSONObject()
        postData.put("participantDeviceID", deviceId)
        postData.put("sessionID", sessionId)

        sendRequest("participantJoinSession", postData) { jsonResponse ->
            callbackParticipantJoinSession(jsonResponse, callback)
        }
    }

    private fun callbackParticipantJoinSession(
        jsonResponse: JSONObject,
        callback: (timestamp: Int, mode: String, artists: List<String>, genres: List<String>) -> Unit
    ) {
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
        callback(timestamp, mode, artists, genres)
    }




    fun participantLeaveSession(onCallbackFinished: () -> Unit = {}) {
        val postData = JSONObject()
        postData.put("participantDeviceID", deviceId)

        sendRequest("participantLeaveSession", postData){
            onCallbackFinished()
        }
    }

}