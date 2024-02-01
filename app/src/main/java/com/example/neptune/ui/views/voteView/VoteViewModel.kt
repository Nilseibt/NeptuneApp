package com.example.neptune.ui.views.voteView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.user.src.User
import com.example.neptune.ui.views.ViewsCollection

class VoteViewModel(
    val participant: User
) : ViewModel() {

    private var leaveSessionDialogShown by mutableStateOf(false)

    fun onToggleUpvote(track: Track) {
        participant.toggleUpvote(track)
    }

    fun getVoteList(): SnapshotStateList<MutableState<Track>> {
        return participant.voteList.value.getTracks()
    }

    fun onSearchTracks(navController: NavController) {
        navController.navigate(ViewsCollection.SEARCH_VIEW.name)
    }

    fun getTopBarDescription(): String {
        return when(participant.session.sessionType){
            SessionType.GENERAL -> "General Mode"
            SessionType.ARTIST -> "Artist Mode"
            SessionType.GENRE -> "Genre Mode"
            SessionType.PLAYLIST -> "Playlist Mode"
        }
    }

    fun onOpenInfo(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }


    fun onOpenStats(navController: NavController) {
        navController.navigate(ViewsCollection.STATS_VIEW.name)
    }

    fun isLeaveSessionDialogShown(): Boolean {
        return leaveSessionDialogShown
    }


    fun onBack(navController: NavController) {
        leaveSessionDialogShown = !leaveSessionDialogShown
    }


    fun onConfirmLeaveSession(navController: NavController) {
        leaveSessionDialogShown = false
        (participant.backendConnector as ParticipantBackendConnector).participantLeaveSession()
        navController.popBackStack(
            ViewsCollection.START_VIEW.name,
            inclusive = false,
            saveState = false
        )
    }


    fun onDismissLeaveSession(navController: NavController) {
        leaveSessionDialogShown = false
    }


    fun syncState(){
        participant.syncState()
    }

}