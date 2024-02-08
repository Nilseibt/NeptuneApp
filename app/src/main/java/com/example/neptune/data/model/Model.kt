package com.example.neptune.data.model

import androidx.navigation.NavController
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.appState.AppDatabase
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.session.SessionBuilder
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.HostSpotifyConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnectionDatabase
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyEstablisher
import com.example.neptune.data.model.streamingConnector.spotifyConnector.StreamingLevel
import com.example.neptune.data.model.user.FullParticipant
import com.example.neptune.data.model.user.Host
import com.example.neptune.data.model.user.UpvoteDatabase
import com.example.neptune.data.model.user.User
import com.example.neptune.data.room.app.AppDataDatabase
import com.example.neptune.data.room.streaming.StreamingConnectionDataDatabase
import com.example.neptune.data.room.upvotes.UpvoteDataDatabase
import com.example.neptune.ui.views.ViewsCollection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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


    private val upvoteDataDatabase by lazy {
        Room.databaseBuilder(
            NeptuneApp.context,
            UpvoteDataDatabase::class.java,
            "upvote_data.db"
        ).build()
    }

    private val upvoteDatabase = UpvoteDatabase(upvoteDataDatabase.upvoteDataDao)


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


    private val backendConnectorVolleyQueue by lazy {
        Volley.newRequestQueue(NeptuneApp.context)
    }


    private var backendConnector: BackendConnector? = null


    private var session: Session? = null


    var appState = AppState(
        streamingEstablisher,
        sessionBuilder,
        appDatabase,
        backendConnector,
        backendConnectorVolleyQueue,
        streamingConnector,
        streamingConnectorVolleyQueue,
        session,
        upvoteDatabase
    )


}