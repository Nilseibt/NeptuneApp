package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.example.neptune.R
import com.example.neptune.data.model.streamingConnector.StreamingEstablisher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URLEncoder
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SpotifyEstablisher(
    private val spotifyConnectionDatabase: SpotifyConnectionDatabase,
    private val volleyQueue: RequestQueue,
    private val context: Context
) : StreamingEstablisher {

    private var accessToken = ""
    private var refreshToken = ""

    private var spotifyLevel = mutableStateOf(StreamingLevel.UNDETERMINED)

    private var artistsSearchList = mutableStateListOf<String>()

    override suspend fun restoreConnectionIfPossible() {
        if (spotifyConnectionDatabase.hasLinkedEntry()) {
            if (spotifyConnectionDatabase.isLinked()) {
                refreshToken = spotifyConnectionDatabase.getRefreshToken()
                if (refreshToken != "") {
                    connectWithRefreshToken()
                } else {
                    initiateConnectWithAuthorize()
                }
            } else {
                spotifyLevel.value = StreamingLevel.UNLINKED
            }
        } else {
            spotifyConnectionDatabase.setLinked(false)
            spotifyLevel.value = StreamingLevel.UNLINKED
        }
    }

    override fun getStreamingLevel(): MutableState<StreamingLevel> {
        return spotifyLevel
    }

    override fun initiateConnectWithAuthorize() {
        val spotifyClientId = context.getString(R.string.spotify_client_id)
        val url = "https://accounts.spotify.com/authorize?client_id=" +
                spotifyClientId +
                "&response_type=code&redirect_uri=" +
                "oauth://neptune-streaming-callback" +
                "&scope=user-modify-playback-state user-read-private"
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun finishConnectWithCode(code: String) {

        val spotifyClientId = context.getString(R.string.spotify_client_id)
        val spotifyClientSecret = context.getString(R.string.spotify_client_secret)
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

    override fun disconnect() {
        GlobalScope.launch {
            spotifyConnectionDatabase.setLinked(false)
            spotifyConnectionDatabase.setRefreshToken("")
        }
        spotifyLevel.value = StreamingLevel.UNLINKED
    }

    override fun searchMatchingArtists(searchInput: String) {

        val baseUrl = "https://api.spotify.com/v1/search"
        val urlSearchQuery = URLEncoder.encode(searchInput, "UTF-8")

        val url = "$baseUrl?q=$urlSearchQuery&type=artist&limit=20"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.i("RES", response.toString())
                matchingArtistsCallback(JSONObject(response))
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

    override fun getArtistSearchList(): SnapshotStateList<String> {
        return artistsSearchList
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
    private fun connectWithRefreshToken() {

        val spotifyClientId = context.getString(R.string.spotify_client_id)
        val spotifyClientSecret = context.getString(R.string.spotify_client_secret)
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
    }

    private fun matchingArtistsCallback(artistsJsonObject: JSONObject) {
        artistsSearchList.clear()
        val artistList = artistsJsonObject.getJSONObject("artists").getJSONArray("items")
        for(index in 0 until artistList.length()){
            artistsSearchList.add(artistList.getJSONObject(index).getString("name"))
        }
    }


}