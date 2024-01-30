package com.example.neptune.data.model.backendConnector

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neptune.data.model.track.src.Track
import org.json.JSONArray
import org.json.JSONObject

class HostBackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) : BackendConnector(deviceId, volleyQueue) {


    fun createNewSession(
        mode: String,
        cooldownTimer: Int,
        artists: List<String>,
        genres: List<String>,
        callback: (sessionId: Int, timestamp: Int) -> Unit
    ) {
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)
        postData.put("modus", mode)
        postData.put("cooldownTimer", cooldownTimer)
        postData.put("artists", JSONArray(artists))
        postData.put("genres", JSONArray(genres))
        postData.put("playlist", "")

        sendRequest("createNewSession", postData) { jsonResponse ->
            callbackCreateNewSession(jsonResponse, callback)
        }
    }

    private fun callbackCreateNewSession(
        jsonResponse: JSONObject,
        callback: (sessionId: Int, timestamp: Int) -> Unit
    ) {
        val sessionId = jsonResponse.getInt("sessionID")
        val timestamp = jsonResponse.getInt("timestamp")
        callback(sessionId, timestamp)
    }




    fun deleteSession() {
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)

        sendRequest("deleteSession", postData)
    }


    fun playedTrack(track: Track) {
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)
        postData.put("trackID", track.id)

        sendRequest("playedTrack", postData)
    }


    fun setBlockTrack(track: Track, blocked: Boolean) {
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)
        postData.put("trackID", track.id)
        postData.put("blocked", blocked)

        sendRequest("setBlockTrack", postData)
    }

}