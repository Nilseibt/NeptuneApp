package com.example.neptune.data.model.streamingConnector.spotifyConnector

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.track.Track
import org.json.JSONObject
import java.net.URLEncoder


/**
 * Class responsible for connecting to the Spotify API and performing various streaming operations.
 * @property volleyQueue The request queue for handling network requests.
 * @property accessToken The access token required for authorization with the Spotify API.
 */
open class SpotifyConnector(
    private val volleyQueue: RequestQueue,
    private val accessToken: String
) : StreamingConnector {

    /**
     * Searches for tracks on Spotify based on the given search input.
     * @param searchInput The search input string.
     * @param resultLimit The maximum number of results to return.
     * @param onCallbackFinished The callback function to handle the search results.
     */
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
        }
        onCallbackFinished(resultList)
    }

    /**
     * Searches for tracks on Spotify based on the given search input and includes genres in the results.
     * @param searchInput The search input string.
     * @param resultLimit The maximum number of results to return.
     * @param onCallbackFinished The callback function to handle the search results.
     */
    override fun searchWithGenres(
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
            searchTracksWithGenresCallback(jsonResponse, onCallbackFinished)
        }
    }

    private fun searchTracksWithGenresCallback(
        jsonResponse: JSONObject,
        onCallbackFinished: (resultList: MutableList<Track>) -> Unit
    ) {
        val artistIds = mutableListOf<String>()

        val resultList = mutableListOf<Track>()
        val tracksJsonList = jsonResponse.getJSONObject("tracks").getJSONArray("items")
        for (index in 0 until tracksJsonList.length()) {

            val trackJson = tracksJsonList.getJSONObject(index)
            val trackId = trackJson.getString("id")
            val trackName = trackJson.getString("name")

            val trackArtistsJsonList = trackJson.getJSONArray("artists")
            val artistNames = mutableListOf<String>()
            for (artistIndex in 0 until trackArtistsJsonList.length()) {
                val artistJson = trackArtistsJsonList.getJSONObject(artistIndex)
                artistNames.add(artistJson.getString("name"))
                artistIds.add(artistJson.getString("id"))
            }

            val trackImageUrl = trackJson.getJSONObject("album").getJSONArray("images")
                .getJSONObject(0).getString("url")

            val track = Track(
                trackId, trackName, artistNames, mutableListOf(), trackImageUrl,
                mutableIntStateOf(0), mutableStateOf(false),
                mutableStateOf(false), mutableStateOf(false)
            )
            resultList.add(track)
        }
        getGenresOfArtists(artistIds){resultMap ->
            resultList.forEach { track ->
                track.artists.forEach { artist ->
                    val genresOfArtist = resultMap[artist]!!
                    genresOfArtist.forEach { genre ->
                        if(genre !in track.genres){
                            track.genres.add(genre)
                        }
                    }
                }
            }
            onCallbackFinished(resultList)
        }
    }


    /**
     * Retrieves the genres of artists associated with the given artist IDs.
     * @param artistIds The list of artist IDs.
     * @param onCallbackFinished The callback function to handle the genre information.
     */
    private fun getGenresOfArtists(
        artistIds: List<String>,
        onCallbackFinished: (resultMap: Map<String, List<String>>) -> Unit
    ) {
        var flattenedIds = ""
        artistIds.forEach {
            flattenedIds += "$it,"
        }
        flattenedIds = flattenedIds.substring(0, flattenedIds.length - 1)

        var headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer $accessToken"

        var parameters: MutableMap<String, String> = HashMap()
        parameters["ids"] = flattenedIds

        newGetRequest(
            "https://api.spotify.com/v1/artists",
            headers,
            parameters
        ) { jsonResponse ->
            genresOfArtistsCallback(jsonResponse, onCallbackFinished)
        }
    }

    private fun genresOfArtistsCallback(
        jsonResponse: JSONObject,
        onCallbackFinished: (resultMap: Map<String, List<String>>) -> Unit
    ) {
        val resultMap = mutableMapOf<String, List<String>>()

        val artistsJsonList = jsonResponse.getJSONArray("artists")
        for (artistIndex in 0 until artistsJsonList.length()) {
            val artistJson = artistsJsonList.getJSONObject(artistIndex)
            val artistName = artistJson.getString("name")
            val genresJsonList = artistJson.getJSONArray("genres")
            val genresList = mutableListOf<String>()
            for (genreIndex in 0 until genresJsonList.length()) {
                genresList.add(genresJsonList.getString(genreIndex))
            }
            resultMap[artistName] = genresList
        }
        onCallbackFinished(resultMap)
    }


    /**
     * Sends a new GET request to the Spotify API.
     * @param url The URL with endpoint for the GET request.
     * @param headers The headers for the request.
     * @param parameters The parameters for the request.
     * @param callback The callback function to handle the JSON response.
     */
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


    /**
     * Sends a new request to the Spotify API.
     * @param url The URL with endpoint for the request.
     * @param method The HTTP method for the request.
     * @param headers The headers for the request.
     * @param parameters The parameters for the request.
     * @param onCallback The optional callback function to execute after the request.
     */
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