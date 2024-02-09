package com.example.neptune.ui.views.startView

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.android.volley.toolbox.Volley
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.StreamingLevel
import com.example.neptune.ui.views.ViewsCollection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * ViewModel class for controlling the logic of the StartView.
 *
 * @property appState The current app state.
 * @property navController The navigation controller used for navigating between different views.
 * @property activity The main activity context.
 */
class StartViewModel(
    val appState: AppState,
    val navController: NavController,
    val activity: MainActivity
) : ViewModel() {


    private var leaveDialogShown by mutableStateOf(false)


    /**
     * Checks if it's possible to create a session.
     *
     * @return True if creating a session is possible, false otherwise.
     */
    fun createSessionPossible(): Boolean {
        return getStreamingLevel().value == StreamingLevel.PREMIUM
    }

    /**
     * Handles the action of toggling connection to Spotify.
     */
    fun onToggleConnectedToSpotify() {
        when (getStreamingLevel().value) {
            StreamingLevel.FREE, StreamingLevel.PREMIUM -> appState.streamingEstablisher.disconnect()
            StreamingLevel.UNLINKED -> appState.streamingEstablisher.initiateConnectWithAuthorize()
            StreamingLevel.UNDETERMINED -> {}
        }
    }

    /**
     * Retrieves the text for the Spotify button based on the current streaming level.
     *
     * @return The text for the Spotify button.
     */
    fun getSpotifyButtonText(): String {
        return when (getStreamingLevel().value) {
            StreamingLevel.FREE, StreamingLevel.PREMIUM -> NeptuneApp.context.getString(R.string.disconnect_spotify)
            StreamingLevel.UNLINKED -> NeptuneApp.context.getString(R.string.connect_spotify)
            StreamingLevel.UNDETERMINED -> ""
        }
    }

    /**
     * Navigates to the join session view.
     *
     * @param navController The NavController instance.
     */
    fun onJoinSession(navController: NavController) {
        navController.navigate(ViewsCollection.JOIN_VIEW.name)
    }

    /**
     * Navigates to the mode select view to create a session.
     *
     * @param navController The NavController instance.
     */
    fun onCreateSession(navController: NavController) {
        navController.navigate(ViewsCollection.MODE_SELECT_VIEW.name)
    }

    /**
     * Checks if the leave dialog is shown.
     *
     * @return True if the leave dialog is shown, false otherwise.
     */
    fun isLeaveDialogShown(): Boolean {
        return leaveDialogShown
    }

    /**
     * Handles the back action, toggling the leave dialog.
     *
     * @param navController The NavController instance.
     */
    fun onBack(navController: NavController) {
        leaveDialogShown = !leaveDialogShown
    }

    /**
     * Confirms leaving the application.
     *
     * @param navController The NavController instance.
     */
    fun onConfirmLeave(navController: NavController) {
        leaveDialogShown = false
        activity.finish()
    }

    /**
     * Dismisses the leave dialog.
     *
     * @param navController The NavController instance.
     */
    fun onDismissLeave(navController: NavController) {
        leaveDialogShown = false
    }


    private fun getStreamingLevel(): MutableState<StreamingLevel> {
        return appState.streamingEstablisher.getStreamingLevel()
    }

    private fun navigateAccordingToUserSessionState(
        userSessionState: String,
        navController: NavController
    ) {
        if (userSessionState == "HOST") {
            navController.navigate(ViewsCollection.CONTROL_VIEW.name)
        }
        if (userSessionState == "PARTICIPANT") {
            navController.navigate(ViewsCollection.VOTE_VIEW.name)
        }
    }

}