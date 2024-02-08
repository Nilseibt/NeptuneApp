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

    fun getTopBarDescription(): SessionType {
        return participant.session.sessionType
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
        if(!navController.popBackStack(
                ViewsCollection.START_VIEW.name,
                inclusive = false,
                saveState = false
            )){
            navController.navigate(ViewsCollection.START_VIEW.name)
        }
    }


    fun onDismissLeaveSession(navController: NavController) {
        leaveSessionDialogShown = false
    }


    fun syncState(navController: NavController){
        if(participant.session.isSessionClosed){
            if(!navController.popBackStack(
                ViewsCollection.START_VIEW.name,
                inclusive = false,
                saveState = false
            )){
                navController.navigate(ViewsCollection.START_VIEW.name)
            }
        }
        participant.syncState()
    }

}