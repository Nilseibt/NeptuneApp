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


    var appState = AppState(streamingEstablisher, sessionBuilder, appDatabase)


    private val backendConnectorVolleyQueue by lazy {
        Volley.newRequestQueue(NeptuneApp.context)
    }


    private var backendConnector: BackendConnector? = null


    private var session: Session? = null


    var user: User? = null


    fun recreateUserSessionStateInitially(
        navController: NavController,
        joinLinkUsed: Boolean,
        onUserNotInSession: () -> Unit
    ) {
        backendConnector = BackendConnector(appState.getDeviceId(), backendConnectorVolleyQueue)
        backendConnector!!.getUserSessionState { userSessionState, sessionId, timestamp, mode, artists, genres ->
            callbackRecreateUserSessionState(
                userSessionState,
                sessionId,
                timestamp,
                mode,
                artists,
                genres,
                navController,
                joinLinkUsed
            )
            if (user == null) {
                onUserNotInSession()
            }
        }
    }

    private fun callbackRecreateUserSessionState(
        userSessionState: String,
        sessionId: Int,
        timestamp: Int,
        mode: String,
        artists: List<String>,
        genres: List<String>,
        navController: NavController,
        joinLinkUsed: Boolean
    ) {
        if (userSessionState == "HOST") {
            backendConnector =
                HostBackendConnector(appState.getDeviceId(), backendConnectorVolleyQueue)
            streamingConnector = HostSpotifyConnector(
                streamingConnectorVolleyQueue,
                streamingEstablisher.getAccessToken(),
                streamingEstablisher.getRefreshToken()
            )

            sessionBuilder.setSessionTypeFromBackendString(mode)
            if (mode == "Artist") {
                sessionBuilder.setSelectedEntities(artists)
            }
            if (mode == "Genre") {
                sessionBuilder.setSelectedEntities(genres)
            }

            session = sessionBuilder.createSession(sessionId, timestamp)
            user = Host(
                session!!,
                backendConnector!! as HostBackendConnector,
                streamingConnector!! as HostStreamingConnector,
                upvoteDatabase
            )
            sessionBuilder.reset()
            navController.navigate(ViewsCollection.CONTROL_VIEW.name)

        }
        if (userSessionState == "PARTICIPANT") {
            backendConnector =
                ParticipantBackendConnector(appState.getDeviceId(), backendConnectorVolleyQueue)

            sessionBuilder.setSessionTypeFromBackendString(mode)
            if (mode == "Artist") {
                sessionBuilder.setSelectedEntities(artists)
            }
            if (mode == "Genre") {
                sessionBuilder.setSelectedEntities(genres)
            }

            session = sessionBuilder.createSession(sessionId, timestamp)

            if (streamingEstablisher.getStreamingLevel().value != StreamingLevel.UNLINKED) {
                streamingConnector = SpotifyConnector(
                    streamingConnectorVolleyQueue,
                    streamingEstablisher.getAccessToken(),
                    streamingEstablisher.getRefreshToken()
                )
                user = FullParticipant(
                    session!!,
                    backendConnector!! as ParticipantBackendConnector,
                    streamingConnector!!,
                    upvoteDatabase
                )
            } else {
                user = User(
                    session!!,
                    backendConnector!! as ParticipantBackendConnector,
                    upvoteDatabase
                )
            }

            sessionBuilder.reset()
            navController.navigate(ViewsCollection.VOTE_VIEW.name)
        }
        if (userSessionState == "NONE") {
            if (!joinLinkUsed) {
                navController.navigate(ViewsCollection.START_VIEW.name)
            }
            user = null
        }
    }

    fun createNewSessionAndJoin(navController: NavController) {
        backendConnector = HostBackendConnector(appState.getDeviceId(), backendConnectorVolleyQueue)
        streamingConnector = HostSpotifyConnector(
            streamingConnectorVolleyQueue, streamingEstablisher.getAccessToken(),
            streamingEstablisher.getRefreshToken()
        )

        val mode = sessionBuilder.getSessionTypeAsBackendString()
        val cooldownTimer = sessionBuilder.getTrackCooldown()
        var artists = listOf<String>()
        if (mode == "Artist") {
            artists = sessionBuilder.getSelectedEntities().toList()
        }
        var genres = listOf<String>()
        if (mode == "Genre") {
            genres = sessionBuilder.getSelectedEntities().toList()
        }

        (backendConnector as HostBackendConnector).createNewSession(
            mode, cooldownTimer, artists, genres
        ) { sessionId, timestamp ->
            session = sessionBuilder.createSession(sessionId, timestamp)
            user = Host(
                session!!,
                backendConnector!! as HostBackendConnector,
                streamingConnector!! as HostStreamingConnector,
                upvoteDatabase
            )
            if (mode == "Playlist") {
                GlobalScope.launch {
                    sessionBuilder.getPlaylistTracks().forEach {
                        user!!.backendConnector.addTrackToSession(it)
                    }
                    sessionBuilder.reset()
                }
            }
            navController.navigate(ViewsCollection.CONTROL_VIEW.name)
        }
    }


    fun tryToJoinSession(sessionId: Int, navController: NavController, onFail: () -> Unit) {
        backendConnector =
            ParticipantBackendConnector(appState.getDeviceId(), backendConnectorVolleyQueue)

        (backendConnector as ParticipantBackendConnector).participantJoinSession(
            sessionId
        ) { success, timestamp, mode, artists, genres ->

            if (!success) {
                onFail()
                return@participantJoinSession
            }

            sessionBuilder.setSessionTypeFromBackendString(mode)
            if (mode == "Artist") {
                sessionBuilder.setSelectedEntities(artists)
            }
            if (mode == "Genre") {
                sessionBuilder.setSelectedEntities(genres)
            }

            session = sessionBuilder.createSession(sessionId, timestamp)

            if (streamingEstablisher.getStreamingLevel().value != StreamingLevel.UNLINKED) {
                streamingConnector = SpotifyConnector(
                    streamingConnectorVolleyQueue,
                    streamingEstablisher.getAccessToken(),
                    streamingEstablisher.getRefreshToken()
                )
                user = FullParticipant(
                    session!!,
                    backendConnector!! as ParticipantBackendConnector,
                    streamingConnector!!,
                    upvoteDatabase
                )
            } else {
                user = User(
                    session!!,
                    backendConnector!! as ParticipantBackendConnector,
                    upvoteDatabase
                )
            }

            sessionBuilder.reset()
            navController.navigate(ViewsCollection.VOTE_VIEW.name)
        }
    }

    fun participantLeaveCurrentSession(onCallbackFinished: () -> Unit) {
        (user!!.backendConnector as ParticipantBackendConnector).participantLeaveSession {
            onCallbackFinished()
        }
    }


    suspend fun deleteIrrelevantUpvotes() {
        val backendConnectorWithoutDeviceId = BackendConnector("", backendConnectorVolleyQueue)
        val sessionsWithUpvotes = upvoteDatabase.getStoredSessions()
        sessionsWithUpvotes.forEach { session ->
            backendConnectorWithoutDeviceId.isSessionOpen(session.first, session.second) { isOpen ->
                if (!isOpen) {
                    GlobalScope.launch {
                        upvoteDatabase.removeAllTracksWithSession(session.first, session.second)
                    }
                }
            }
        }
    }


    suspend fun refreshSpotifyConnection() {
        streamingEstablisher.restoreConnectionIfPossible {
            if (user is Host) {
                streamingConnector = HostSpotifyConnector(
                    streamingConnectorVolleyQueue, streamingEstablisher.getAccessToken(),
                    streamingEstablisher.getRefreshToken()
                )
            } else if (user is FullParticipant) {
                streamingConnector = SpotifyConnector(
                    streamingConnectorVolleyQueue,
                    streamingEstablisher.getAccessToken(),
                    streamingEstablisher.getRefreshToken()
                )
            }
        }
    }

}