package com.example.neptune.data.model.backendConnector

import com.android.volley.RequestQueue
import com.example.neptune.data.model.track.Track
import org.json.JSONArray
import org.json.JSONObject

/**
 * Connector class for host-specific backend interactions, extending the base [BackendConnector].
 * @param deviceId The unique identifier of this host device.
 * @param volleyQueue The Volley request queue for network communication.
 */
class HostBackendConnector(
    private val deviceId: String,
    private val volleyQueue: RequestQueue
) : BackendConnector(deviceId, volleyQueue) {


    /**
     * Creates a new session with the specified session parameters.
     * @param mode The mode of the session ("General", "Artist", "Genre", "Playlist").
     * @param cooldownTimer The cooldown timer duration for played tracks in the session.
     * @param artists The list of artists associated with the session, should be empty if not artist session.
     * @param genres The list of genres associated with the session.
     * @param callback Callback function to handle the response containing session ID and timestamp.
     */
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



    /**
     * Deletes the current session, causes participants to be removed from the deleted session.
     */
    fun deleteSession() {
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)

        sendRequest("deleteSession", postData)
    }


    /**
     * Indicates that a track has been played within the session, activating the cooldown.
     * @param track The track that has been played.
     */
    fun playedTrack(track: Track) {
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)
        postData.put("trackID", track.id)

        sendRequest("playedTrack", postData)
    }


    /**
     * Sets the blocked status of a track within the session.
     * @param track The track to set the blocked status for.
     * @param blocked Boolean value indicating whether the track is blocked.
     */
    fun setBlockTrack(track: Track, blocked: Boolean) {
        val postData = JSONObject()
        postData.put("hostDeviceID", deviceId)
        postData.put("trackID", track.id)
        postData.put("blocked", blocked)

        sendRequest("setBlockTrack", postData)
    }

}