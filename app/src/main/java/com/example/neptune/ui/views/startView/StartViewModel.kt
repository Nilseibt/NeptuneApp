package com.example.neptune.ui.views.startView

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.android.volley.toolbox.Volley
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.StreamingLevel
import com.example.neptune.ui.views.ViewsCollection

class StartViewModel(
    val appState: AppState
) : ViewModel() {


    private var leaveDialogShown by mutableStateOf(false)

    fun createSessionPossible(): Boolean {
        return getStreamingLevel().value == StreamingLevel.PREMIUM
    }

    fun onToggleConnectedToSpotify() {
        when (getStreamingLevel().value) {
            StreamingLevel.FREE, StreamingLevel.PREMIUM -> appState.streamingEstablisher.disconnect()
            StreamingLevel.UNLINKED -> appState.streamingEstablisher.initiateConnectWithAuthorize()
            StreamingLevel.UNDETERMINED -> {}
        }
    }

    fun getSpotifyButtonText(): String {
        return when (getStreamingLevel().value) {
            StreamingLevel.FREE, StreamingLevel.PREMIUM -> "Von Spotify trennen"
            StreamingLevel.UNLINKED -> "Mit Spotify verknüpfen"
            StreamingLevel.UNDETERMINED -> ""
        }
    }

    fun onJoinSession(navController: NavController) {
        navController.navigate(ViewsCollection.JOIN_VIEW.name)
    }

    fun onCreateSession(navController: NavController) {
        navController.navigate(ViewsCollection.MODE_SELECT_VIEW.name)
    }

    fun isLeaveDialogShown(): Boolean {
        return leaveDialogShown
    }

    fun onBack(navController: NavController) {
        leaveDialogShown = !leaveDialogShown
    }

    fun onConfirmLeave(navController: NavController) {
        leaveDialogShown = false
        navController.popBackStack()
    }

    fun onDismissLeave(navController: NavController) {
        leaveDialogShown = false
    }


    private fun getStreamingLevel(): MutableState<StreamingLevel> {
        return appState.streamingEstablisher.getStreamingLevel()
    }

}