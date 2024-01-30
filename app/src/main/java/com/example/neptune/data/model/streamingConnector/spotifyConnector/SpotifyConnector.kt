package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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

    override fun search(searchInput: String, searchList: TrackList) {

        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()
        headers["q"] = URLEncoder.encode(searchInput, "UTF-8")
        headers["type"] = "track"
        headers["limit"] = "20"

        newRequest("https://api.spotify.com/v1/search", headers, parameters) { jsonResponse ->
            searchTracksCallback(jsonResponse, searchList)
        }
    }

    private fun searchTracksCallback(jsonResponse: JSONObject, searchList: TrackList) {

        searchList.clear()
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

            //TODO upvotes and block
            val track = Track(trackId, trackName, artistNames, listOf(), trackImageUrl, 0, false, false)
            searchList.addTrack(track)
        }
    }


    protected fun newRequest(
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
            Request.Method.GET, url,
            { response ->
                Log.i("SPOTIFY JSON", "$url $response")
                callback(JSONObject(response))
            },
            { error ->
                Log.e("SPOTIFY VOLLEY", "$url : Spotify Request Error: ${error.localizedMessage}")
            }) {
            override fun getHeaders(): Map<String, String> {
                return headers
            }
        }
        volleyQueue.add(stringRequest)
    }


}