package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.track.src.TrackList
import org.json.JSONObject
import java.net.URLEncoder

open class SpotifyConnector(
    private val volleyQueue: RequestQueue,
    private val accessToken: String,
    private val refreshToken: String
) : StreamingConnector {

    override fun search(
        searchInput: String,
        resultLimit: Int,
        onCallbackFinished: (resultList: MutableList<Track>) -> Unit
    ) {

        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()
        parameters["q"] = URLEncoder.encode(searchInput, "UTF-8")
        parameters["type"] = "track"
        parameters["limit"] = resultLimit.toString()

        newGetRequest(
            "https://api.spotify.com/v1/search",
            headers,
            parameters
        ) { jsonResponse ->
            searchTracksCallback(jsonResponse, onCallbackFinished)
        }
    }

    private fun searchTracksCallback(
        jsonResponse: JSONObject,
        onCallbackFinished: (resultList: MutableList<Track>) -> Unit
    ) {

        val resultList = mutableListOf<Track>()
        val tracksJsonList = jsonResponse.getJSONObject("tracks").getJSONArray("items")
        for (index in 0 until tracksJsonList.length()) {
            val trackJson = tracksJsonList.getJSONObject(index)
            val trackId = trackJson.getString("id")
            val trackName = trackJson.getString("name")
            val trackArtistsJsonList = trackJson.getJSONArray("artists")
            val artistNames = mutableListOf<String>()
            for (artistIndex in 0 until trackArtistsJsonList.length()) {
                artistNames.add(trackArtistsJsonList.getJSONObject(artistIndex).getString("name"))
            }
            val trackImageUrl = trackJson.getJSONObject("album").getJSONArray("images")
                .getJSONObject(0).getString("url")

            val track = Track(
                trackId, trackName, artistNames, listOf(), trackImageUrl,
                mutableIntStateOf(0), mutableStateOf(false),
                mutableStateOf(false), mutableStateOf(false)
            )
            resultList.add(track)
        }
        onCallbackFinished(resultList)
    }


    override fun searchWithGenre(
        searchInput: String,
        onCallbackFinished: (resultList: MutableList<Track>) -> Unit
    ) {
        search(searchInput, 10){
            //TODO actually search for the genre
            onCallbackFinished(it)
        }
    }


    protected fun newGetRequest(
        url: String,
        headers: Map<String, String> = mapOf(),
        parameters: Map<String, String>,
        callback: (jsonResponse: JSONObject) -> Unit
    ) {
        var urlWithParams = url
        if (parameters.isNotEmpty()) {
            urlWithParams += "?"
            parameters.forEach {
                urlWithParams += it.key + "=" + it.value + "&"
            }
            urlWithParams.dropLast(1)
        }
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, urlWithParams,
            { response ->
                Log.i(
                    "SPOTIFY JSON",
                    "$urlWithParams ${response.subSequence(0, minOf(response.length, 50))}"
                )
                if(response != "") {
                    callback(JSONObject(response))
                }
            },
            { error ->
                Log.e("SPOTIFY VOLLEY", "$urlWithParams : Spotify Request Error: ${error}")
            }) {
            override fun getHeaders(): Map<String, String> {
                return headers
            }
        }
        volleyQueue.add(stringRequest)
    }

    protected fun newRequest(
        url: String,
        method: Int,
        headers: Map<String, String> = mapOf(),
        parameters: Map<String, String>,
        onCallback: () -> Unit = {}
    ) {
        var urlWithParams = url
        if (parameters.isNotEmpty()) {
            urlWithParams += "?"
            parameters.forEach {
                urlWithParams += it.key + "=" + it.value + "&"
            }
            urlWithParams.dropLast(1)
        }
        val stringRequest: StringRequest = object : StringRequest(
            method, urlWithParams,
            { response ->
                Log.i(
                    "SPOTIFY JSON",
                    "$urlWithParams $response"
                )
                onCallback()
            },
            { error ->
                Log.e("SPOTIFY VOLLEY", "$urlWithParams : Spotify Request Error: ${error}")
            }) {
            override fun getHeaders(): Map<String, String> {
                return headers
            }
        }
        volleyQueue.add(stringRequest)
    }


}