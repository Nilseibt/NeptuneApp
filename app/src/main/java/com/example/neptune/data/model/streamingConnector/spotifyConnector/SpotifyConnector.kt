package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.android.volley.Request
import com.android.volley.RequestQueue
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

        //TODO started implementing here, but the draft got me, nothing works -Nils
        /*val baseUrl = "https://api.spotify.com/v1/search"
        val urlSearchQuery = URLEncoder.encode(searchInput, "UTF-8")

        val url = "$baseUrl?q=$urlSearchQuery&type=track&limit=20"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.i("RES", response.toString())
                searchTracksCallback(JSONObject(response), searchList)
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

        volleyQueue.add(stringRequest)*/
    }

    private fun searchTracksCallback(artistsJsonObject: JSONObject, searchList: TrackList) {

        //TODO started implementing here, but the draft got me, nothing works -Nils
        //TODO ATTENTION: Code down here does not compile!!!
        /*searchList.clear()
        val trackList = mutableListOf<Track>()
        val tracksJsonList = artistsJsonObject.getJSONObject("tracks").getJSONArray("items")
        for(index in 0 until tracksJsonList.length()){
            val trackJson = tracksJsonList.getJSONObject(index)
            val trackId = trackJson.getString("id")
            val trackName = trackJson.getString("name")
            val trackArtistsJsonList = trackJson.getJSONArray("artists")
            val artistNames = mutableListOf<String>()
            for(artistIndex in 0 until trackArtistsJsonList.length()){
                artistNames.add(trackArtistsJsonList.getJSONObject(artistIndex).getString("name"))
            }

            val track = Track(trackId, trackName)
            trackList.add(artistList.getJSONObject(index).getString("name"))
        }*/
    }


}