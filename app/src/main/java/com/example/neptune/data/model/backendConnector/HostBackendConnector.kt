package com.example.neptune.data.model.backendConnector

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class HostBackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) : BackendConnector(deviceId, volleyQueue) {


    fun createNewSession(callback: (sessionId: Int, timestamp: Int) -> Unit) {
        //TODO does not work yet
        return
        /*val url = baseUrl + "createNewSession"
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)
        postData.put("modus", "General")
        postData.put("cooldownTimer", 0)
        postData.put("artists", JSONArray())
        postData.put("genres", JSONArray())
        postData.put("playlist", "")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            { response ->
                Log.i("JSON CREATE", response.toString())
                val sessionId = response.getInt("sessionID")
                val timestamp = response.getInt("timestamp")
                callback(sessionId, timestamp)
            },
            { error ->
                Log.e("VOLLEY", "Server Request Error: ${error.message}")
            })

        volleyQueue.add(jsonObjectRequest)*/
    }


    fun deleteSession() {
        val url = baseUrl + "deleteSession"
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            { response ->
                Log.i("JSON DEL", response.toString()) },
            { error ->
                Log.e("VOLLEY", "Server Request Error: ${error.localizedMessage}")
            })

        volleyQueue.add(jsonObjectRequest)
    }


    suspend fun playedTrack() {
        //TODO needs implementation
    }


    suspend fun setBlockTrack() {
        //TODO needs implementation
    }

}