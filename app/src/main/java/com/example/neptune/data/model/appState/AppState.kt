package com.example.neptune.data.model.appState

import android.content.Context
import android.net.wifi.WifiManager
import androidx.navigation.NavController
import com.android.volley.RequestQueue
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.session.SessionBuilder
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.streamingConnector.StreamingEstablisher
import com.example.neptune.data.model.streamingConnector.spotifyConnector.HostSpotifyConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.StreamingLevel
import com.example.neptune.data.model.user.FullParticipant
import com.example.neptune.data.model.user.Host
import com.example.neptune.data.model.user.UpvoteDatabase
import com.example.neptune.data.model.user.User
import com.example.neptune.ui.views.ViewsCollection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AppState(
    val streamingEstablisher: StreamingEstablisher,
    var sessionBuilder: SessionBuilder,
    private val appDatabase: AppDatabase,
    private var backendConnector: BackendConnector?,
    private val backendConnectorVolleyQueue: RequestQueue,
    private var streamingConnector: StreamingConnector?,
    private val streamingConnectorVolleyQueue: RequestQueue,
    private var session: Session?,
    private val upvoteDatabase: UpvoteDatabase
) {

    var user: User? = null

    var initialBackendConnector: BackendConnector? = null



    private var deviceId = ""


    fun recreateUserSessionStateInitially(
        navController: NavController,
        joinLinkUsed: Boolean,
        onUserNotInSession: () -> Unit
    ) {
        initialBackendConnector = BackendConnector(getDeviceId(), backendConnectorVolleyQueue)
        initialBackendConnector!!.getUserSessionState { userSessionState, sessionId, timestamp, mode, artists, genres ->
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
                HostBackendConnector(getDeviceId(), backendConnectorVolleyQueue)
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
                ParticipantBackendConnector(getDeviceId(), backendConnectorVolleyQueue)

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
        backendConnector = HostBackendConnector(getDeviceId(), backendConnectorVolleyQueue)
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
            ParticipantBackendConnector(getDeviceId(), backendConnectorVolleyQueue)

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


    fun getDeviceId(): String{
        return deviceId
    }


    suspend fun generateOrRetrieveDeviceId() {
        if (appDatabase.hasDeviceId()) {
            deviceId = appDatabase.getDeviceId()
        } else {
            deviceId = generateDeviceId()
            appDatabase.setDeviceId(deviceId)
        }
    }

    private fun generateDeviceId(): String {
        val wifiManager = NeptuneApp.context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val macAddress = wifiManager.connectionInfo.macAddress
        val randomAddition = (0..Int.MAX_VALUE).random().toString()
        return generateSHA256(macAddress + randomAddition)
    }

    private fun generateSHA256(input: String): String {
        val bytes = input.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        val hexString = StringBuffer()
        for (byte in digest) {
            val hex = Integer.toHexString(0xff and byte.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}