package com.example.neptune.data.model

import android.content.Context
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.example.neptune.data.model.appState.AppDatabase
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.streamingConnector.StreamingConnectionDatabase
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnectionDatabase
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyEstablisher
import com.example.neptune.data.room.app.AppDataDatabase
import com.example.neptune.data.room.streaming.StreamingConnectionDataDatabase

class Model(
    private val context: Context
) {

    private val appDataDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDataDatabase::class.java,
            "app_data.db"
        ).build()
    }

    private val appDatabase = AppDatabase(appDataDatabase.appDataDao)


    private val streamingConnectionDataDatabase by lazy {
        Room.databaseBuilder(
            context,
            StreamingConnectionDataDatabase::class.java,
            "streaming_connection_data.db"
        ).build()
    }


    private val streamingConnectionDatabase =
        SpotifyConnectionDatabase(streamingConnectionDataDatabase.streamingConnectionDataDao)


    private val streamingConnectorVolleyQueue by lazy {
        Volley.newRequestQueue(context)
    }


    private val streamingEstablisher by lazy {
        SpotifyEstablisher(
            streamingConnectionDatabase,
            streamingConnectorVolleyQueue,
            context
        )
    }


    private var streamingConnector: StreamingConnector? = null


    var appState = AppState(streamingEstablisher, appDatabase, context)

}