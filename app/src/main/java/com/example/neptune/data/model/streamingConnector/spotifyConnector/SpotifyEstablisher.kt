package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.streamingConnector.StreamingEstablisher
import com.example.neptune.data.model.track.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URLEncoder
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Class responsible for establishing and managing the connection with the Spotify streaming service.
 * @param spotifyConnectionDatabase The database interface for managing Spotify connection data.
 * @param volleyQueue The request queue for making network requests.
 */
class SpotifyEstablisher(
    private val spotifyConnectionDatabase: SpotifyConnectionDatabase,
    private val volleyQueue: RequestQueue
) : StreamingEstablisher {

    private var accessToken = ""
    private var refreshToken = ""

    private var spotifyLevel = mutableStateOf(StreamingLevel.UNDETERMINED)

    private var onRestoreFinished: () -> Unit = {}

    /**
     * Retrieves the access token.
     * @return The access token.
     */
    override fun getAccessToken(): String {
        return accessToken
    }

    /**
     * Retrieves the refresh token.
     * @return The refresh token.
     */
    override fun getRefreshToken(): String {
        return refreshToken
    }


    /**
     * Attempts to restore the connection if possible.
     * @param onRestoreFinished Callback invoked after attempting to restore the connection.
     */
    override suspend fun restoreConnectionIfPossible(onRestoreFinished: () -> Unit) {
        this.onRestoreFinished = onRestoreFinished
        if (spotifyConnectionDatabase.hasLinkedEntry()) {
            if (spotifyConnectionDatabase.isLinked()) {
                refreshToken = spotifyConnectionDatabase.getRefreshToken()
                if (refreshToken != "") {
                    connectWithRefreshToken {
                        spotifyLevel.value = StreamingLevel.UNLINKED
                        GlobalScope.launch {
                            spotifyConnectionDatabase.setLinked(false)
                            onRestoreFinished()
                        }
                    }
                } else {
                    initiateConnectWithAuthorize()
                }
            } else {
                spotifyLevel.value = StreamingLevel.UNLINKED
                onRestoreFinished()
            }
        } else {
            spotifyConnectionDatabase.setLinked(false)
            spotifyLevel.value = StreamingLevel.UNLINKED
            onRestoreFinished()
        }
    }


    /**
     * Retrieves the streaming level.
     * @return The mutable state of the streaming level.
     */
    override fun getStreamingLevel(): MutableState<StreamingLevel> {
        return spotifyLevel
    }


    /**
     * Initiates the connection process with Spotify using the authorization flow in a custom tab.
     */
    override fun initiateConnectWithAuthorize() {
        val spotifyClientId = NeptuneApp.context.getString(R.string.spotify_client_id)
        val url = "https://accounts.spotify.com/authorize?client_id=" +
                spotifyClientId +
                "&response_type=code&redirect_uri=" +
                "oauth://neptune-streaming-callback" +
                "&scope=user-modify-playback-state user-read-private user-read-playback-state"
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(NeptuneApp.context, Uri.parse(url))
    }


    /**
     * Completes the connection process with Spotify using the provided authorization code.
     * @param code The authorization code.
     */
    @OptIn(ExperimentalEncodingApi::class)
    override fun finishConnectWithCode(code: String) {

        val spotifyClientId = NeptuneApp.context.getString(R.string.spotify_client_id)
        val spotifyClientSecret = NeptuneApp.context.getString(R.string.spotify_client_secret)
        val toEncode = "$spotifyClientId:$spotifyClientSecret"
        val encodedCredentials = "Basic " + Base64.encode(toEncode.toByteArray())
        val url = "https://accounts.spotify.com/api/token"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                val responseJsonObject = JSONObject(response)
                accessAndRefreshTokenCallback(
                    responseJsonObject.getString("access_token"),
                    responseJsonObject.getString("refresh_token")
                )
            },
            { error ->
                Log.e("VOLLEY", "Spotify Request Error: ${String(error.networkResponse.data)}")
            }) {
            override fun getHeaders(): Map<String, String> {
                var params: MutableMap<String, String> = HashMap()
                params["Authorization"] = encodedCredentials
                return params
            }

            override fun getParams(): Map<String, String> {
                var params: MutableMap<String, String> = HashMap()
                params["grant_type"] = "authorization_code"
                params["code"] = code
                params["redirect_uri"] = "oauth://neptune-streaming-callback"
                return params
            }

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }
        }

        volleyQueue.add(stringRequest)
    }


    /**
     * Disconnects/unlinks from the Spotify streaming service, new connection requires new authentication.
     */
    override fun disconnect() {
        GlobalScope.launch {
            spotifyConnectionDatabase.setLinked(false)
            spotifyConnectionDatabase.setRefreshToken("")
        }
        spotifyLevel.value = StreamingLevel.UNLINKED
    }


    /**
     * Searches for artists matching the provided search input.
     * @param searchInput The search input to find matching artists.
     * @param callback The callback to handle the list of matching artists.
     */
    override fun searchMatchingArtists(searchInput: String, callback: (List<String>) -> Unit) {

        val baseUrl = "https://api.spotify.com/v1/search"
        val urlSearchQuery = URLEncoder.encode(searchInput, "UTF-8")

        val url = "$baseUrl?q=$urlSearchQuery&type=artist&limit=20"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, url,
            { response ->
                matchingArtistsCallback(JSONObject(response), callback)
            },
            { error ->
                Log.e("VOLLEY", "Spotify Request Error: ${String(error.networkResponse.data)}")
            }) {
            override fun getHeaders(): Map<String, String> {
                var params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $accessToken"
                return params
            }
        }

        volleyQueue.add(stringRequest)
    }


    /**
     * Retrieves the playlist from Spotify with the specified playlist ID.
     * @param playlistId The ID of the playlist to retrieve.
     * @param callback The callback to handle the retrieved playlist.
     */
    override fun getPlaylist(playlistId: String, callback: (MutableList<Track>) -> Unit) {
        val url = "https://api.spotify.com/v1/playlists/$playlistId"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, url,
            { response ->
                playlistCallback(JSONObject(response), callback)
            },
            { error ->
                Log.e("VOLLEY", "Spotify Request Error: ${String(error.networkResponse.data)}")
            }) {
            override fun getHeaders(): Map<String, String> {
                var params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $accessToken"
                return params
            }
        }
        volleyQueue.add(stringRequest)
    }


    private fun accessAndRefreshTokenCallback(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        GlobalScope.launch {
            spotifyConnectionDatabase.setLinked(true)
            spotifyConnectionDatabase.setRefreshToken(refreshToken)
        }
        Log.i("LINKED, TOKEN:", accessToken)
        determineAndSetSpotifyLevel()
    }


    @OptIn(ExperimentalEncodingApi::class)
    private fun connectWithRefreshToken(onFail: () -> Unit) {

        val spotifyClientId = NeptuneApp.context.getString(R.string.spotify_client_id)
        val spotifyClientSecret = NeptuneApp.context.getString(R.string.spotify_client_secret)
        val toEncode = "$spotifyClientId:$spotifyClientSecret"
        val encodedCredentials = "Basic " + Base64.encode(toEncode.toByteArray())
        val url = "https://accounts.spotify.com/api/token"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                val responseJsonObject = JSONObject(response)
                accessTokenCallback(
                    responseJsonObject.getString("access_token")
                )
            },
            { error ->
                Log.e("VOLLEY", "Spotify Request Error: ${String(error.networkResponse.data)}")
                onFail()
            }) {
            override fun getHeaders(): Map<String, String> {
                var params: MutableMap<String, String> = HashMap()
                params["Authorization"] = encodedCredentials
                return params
            }

            override fun getParams(): Map<String, String> {
                var params: MutableMap<String, String> = HashMap()
                params["grant_type"] = "refresh_token"
                params["refresh_token"] = refreshToken
                return params
            }

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }
        }

        volleyQueue.add(stringRequest)
    }


    private fun accessTokenCallback(accessToken: String) {
        this.accessToken = accessToken
        Log.i("LINKED, TOKEN:", accessToken)
        determineAndSetSpotifyLevel()
    }


    private fun determineAndSetSpotifyLevel() {

        val url = "https://api.spotify.com/v1/me"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, url,
            { response ->
                val responseJsonObject = JSONObject(response)
                setSpotifyLevelCallback(
                    responseJsonObject.getString("product")
                )
            },
            { error ->
                Log.e("VOLLEY", "Spotify Request Error: ${String(error.networkResponse.data)}")
            }) {
            override fun getHeaders(): Map<String, String> {
                var params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $accessToken"
                return params
            }
        }

        volleyQueue.add(stringRequest)
    }

    private fun setSpotifyLevelCallback(spotifyLevelString: String) {
        if (spotifyLevelString == "premium") {
            spotifyLevel.value = StreamingLevel.PREMIUM
        } else if (spotifyLevelString == "free" || spotifyLevelString == "open") {
            spotifyLevel.value = StreamingLevel.FREE
        }
        Log.i("DETERMINED LEVEL", spotifyLevel.toString())
        onRestoreFinished()
    }

    private fun matchingArtistsCallback(
        artistsJsonObject: JSONObject,
        callback: (List<String>) -> Unit
    ) {
        val artistsSearchList = mutableListOf<String>()
        val artistList = artistsJsonObject.getJSONObject("artists").getJSONArray("items")
        for (index in 0 until artistList.length()) {
            artistsSearchList.add(artistList.getJSONObject(index).getString("name"))
        }
        callback(artistsSearchList)
    }


    private fun playlistCallback(
        jsonResponse: JSONObject,
        callback: (MutableList<Track>) -> Unit
    ) {
        val resultList = mutableListOf<Track>()
        val tracksJsonList = jsonResponse.getJSONObject("tracks").getJSONArray("items")
        Log.i("LOG", tracksJsonList.length().toString())
        for (index in 0 until tracksJsonList.length()) {

            try {
                val trackJson = tracksJsonList.getJSONObject(index).getJSONObject("track")
                val trackId = trackJson.getString("id")
                val trackName = trackJson.getString("name")

                val trackArtistsJsonList = trackJson.getJSONArray("artists")
                val artistNames = mutableListOf<String>()
                for (artistIndex in 0 until trackArtistsJsonList.length()) {
                    val artistJson = trackArtistsJsonList.getJSONObject(artistIndex)
                    artistNames.add(artistJson.getString("name"))
                }

                val trackImageUrl = trackJson.getJSONObject("album").getJSONArray("images")
                    .getJSONObject(0).getString("url")

                val track = Track(
                    trackId, trackName, artistNames, mutableListOf(), trackImageUrl,
                    mutableIntStateOf(0), mutableStateOf(false),
                    mutableStateOf(false), mutableStateOf(false)
                )
                resultList.add(track)
            } catch (exception: Exception) {
            }
        }
        callback(resultList)
    }

}