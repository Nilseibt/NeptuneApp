package com.example.neptune.data.model.backendConnector

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

/**
 * Connector class for participant-specific backend interactions, extending the base [BackendConnector].
 * @param deviceId The unique identifier of this participant device.
 * @param volleyQueue The Volley request queue for network communication.
 */
class ParticipantBackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) : BackendConnector(deviceId, volleyQueue) {


    /**
     * Allows a participant to join a session with the specified session ID.
     * @param sessionId The ID of the session to join.
     * @param callback Callback function to handle the response of the join operation, containing information on the session.
     */
    fun participantJoinSession(
        sessionId: Int,
        callback: (success: Boolean, timestamp: Int, mode: String, artists: List<String>, genres: List<String>) -> Unit
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
        callback: (success: Boolean, timestamp: Int, mode: String, artists: List<String>, genres: List<String>) -> Unit
    ) {
        val status = jsonResponse.getString("status")

        if (status == "success") {

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
            callback(true, timestamp, mode, artists, genres)
        } else {
            val error = jsonResponse.getString("error")
            if (error == "Session does not exist") {
                callback(false, -1, "", listOf(), listOf())
            }
        }
    }


    /**
     * Allows a participant to leave the current session.
     * @param onCallbackFinished Callback function to execute after leaving the session successfully.
     */
    fun participantLeaveSession(onCallbackFinished: () -> Unit = {}) {
        val postData = JSONObject()
        postData.put("participantDeviceID", deviceId)

        sendRequest("participantLeaveSession", postData) {
            onCallbackFinished()
        }
    }

}