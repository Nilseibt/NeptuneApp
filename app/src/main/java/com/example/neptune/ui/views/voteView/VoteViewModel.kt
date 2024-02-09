package com.example.neptune.ui.views.voteView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.track.Track
import com.example.neptune.data.model.user.User
import com.example.neptune.ui.views.ViewsCollection


/**
 * ViewModel class for controlling the logic of the VoteView.
 *
 * @property user The user of the current session.
 */
class VoteViewModel(
    val participant: User
) : ViewModel() {

    private var leaveSessionDialogShown by mutableStateOf(false)

    /**
     * Toggles the upvote status for the given track.
     *
     * @param track The track to toggle the upvote status for.
     */
    fun onToggleUpvote(track: Track) {
        participant.toggleUpvote(track)
    }

    /**
     * Retrieves the list of tracks available for voting.
     *
     * @return A SnapshotStateList containing mutable states of tracks available for voting.
     */
    fun getVoteList(): SnapshotStateList<MutableState<Track>> {
        return participant.voteList.value.getTracks()
    }

    /**
     * Navigates to the search tracks view.
     *
     * @param navController The NavController used for navigation.
     */
    fun onSearchTracks(navController: NavController) {
        navController.navigate(ViewsCollection.SEARCH_VIEW.name)
    }

    /**
     * Retrieves the session type for displaying in the top bar.
     *
     * @return The session type associated with the participant's session.
     */
    fun getTopBarDescription(): SessionType {
        return participant.session.sessionType
    }

    /**
     * Navigates to the info view.
     *
     * @param navController The NavController used for navigation.
     */
    fun onOpenInfo(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }

    /**
     * Navigates to the stats view.
     *
     * @param navController The NavController used for navigation.
     */
    fun onOpenStats(navController: NavController) {
        navController.navigate(ViewsCollection.STATS_VIEW.name)
    }

    /**
     * Checks if the leave session dialog is shown.
     *
     * @return true if the leave session dialog is shown, false otherwise.
     */
    fun isLeaveSessionDialogShown(): Boolean {
        return leaveSessionDialogShown
    }

    /**
     * Handles the back action.
     * Toggles the state of the leave session dialog.
     *
     * @param navController The NavController used for navigation.
     */
    fun onBack(navController: NavController) {
        leaveSessionDialogShown = !leaveSessionDialogShown
    }

    /**
     * Confirms leaving the session and navigates to the start view.
     *
     * @param navController The NavController used for navigation.
     */
    fun onConfirmLeaveSession(navController: NavController) {
        leaveSessionDialogShown = false
        (participant.backendConnector as ParticipantBackendConnector).participantLeaveSession()
        if (!navController.popBackStack(
                ViewsCollection.START_VIEW.name,
                inclusive = false,
                saveState = false
            )
        ) {
            navController.navigate(ViewsCollection.START_VIEW.name)
        }
    }

    /**
     * Dismisses the leave session dialog.
     */
    fun onDismissLeaveSession(navController: NavController) {
        leaveSessionDialogShown = false
    }

    /**
     * Synchronizes the state with the backend.
     *
     * @param navController The NavController used for navigation.
     */
    fun syncState(navController: NavController) {
        if (participant.session.isSessionClosed) {
            if (!navController.popBackStack(
                    ViewsCollection.START_VIEW.name,
                    inclusive = false,
                    saveState = false
                )
            ) {
                navController.navigate(ViewsCollection.START_VIEW.name)
            }
        }
        participant.syncState()
    }

}