package com.example.neptune.data.model

import android.content.Context
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.appState.AppDatabase
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.session.SessionBuilder
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.StreamingConnectionDatabase
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.HostSpotifyConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnectionDatabase
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyEstablisher
import com.example.neptune.data.model.user.src.Host
import com.example.neptune.data.model.user.src.User
import com.example.neptune.data.room.app.AppDataDatabase
import com.example.neptune.data.room.streaming.StreamingConnectionDataDatabase

class Model() {

    private val appDataDatabase by lazy {
        Room.databaseBuilder(
            NeptuneApp.context,
            AppDataDatabase::class.java,
            "app_data.db"
        ).build()
    }

    private val appDatabase = AppDatabase(appDataDatabase.appDataDao)


    private val streamingConnectionDataDatabase by lazy {
        Room.databaseBuilder(
            NeptuneApp.context,
            StreamingConnectionDataDatabase::class.java,
            "streaming_connection_data.db"
        ).build()
    }


    private val streamingConnectionDatabase =
        SpotifyConnectionDatabase(streamingConnectionDataDatabase.streamingConnectionDataDao)


    private val streamingConnectorVolleyQueue by lazy {
        Volley.newRequestQueue(NeptuneApp.context)
    }


    private val streamingEstablisher by lazy {
        SpotifyEstablisher(
            streamingConnectionDatabase,
            streamingConnectorVolleyQueue
        )
    }


    private val sessionBuilder = SessionBuilder()


    private var streamingConnector: StreamingConnector? = null


    var appState = AppState(streamingEstablisher, sessionBuilder, appDatabase)


    private val backendConnectorVolleyQueue by lazy {
        Volley.newRequestQueue(NeptuneApp.context)
    }


    private var backendConnector: BackendConnector? = null


    private var session: Session? = null


    var user: User? = null


    fun createNewSessionAndJoin() {
        backendConnector = HostBackendConnector(appState.getDeviceId(), backendConnectorVolleyQueue)
        streamingConnector = HostSpotifyConnector(streamingConnectorVolleyQueue, "", "")
        //TODO pass tokens

        val mode = when (sessionBuilder.getSessionType()) {
            SessionType.GENERAL -> "General"
            SessionType.ARTIST -> "Artist"
            SessionType.GENRE -> "Genre"
            SessionType.PLAYLIST -> "Playlist"
        }
        val cooldownTimer = sessionBuilder.getTrackCooldown()
        var artists = listOf<String>()
        if (mode == "Artist") {
            artists = sessionBuilder.getSelectedEntities().toList()
        }
        var genres = listOf<String>()
        if (mode == "Genres") {
            genres = sessionBuilder.getSelectedEntities().toList()
        }

        (backendConnector as HostBackendConnector).createNewSession(
            mode, cooldownTimer, artists, genres
        ) { sessionId, timestamp ->
            session = sessionBuilder.createSession(sessionId, timestamp)
            user = Host(
                session!!,
                backendConnector!! as HostBackendConnector,
                streamingConnector!! as HostStreamingConnector
            )
            sessionBuilder.reset()
        }
    }

}