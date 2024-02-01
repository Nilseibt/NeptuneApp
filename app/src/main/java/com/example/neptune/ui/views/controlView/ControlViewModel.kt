package com.example.neptune.ui.views.controlView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.user.src.Host
import com.example.neptune.ui.views.ViewsCollection

class ControlViewModel(
    val host: Host
) : ViewModel() {

    private var deleteSessionDialogShown by mutableStateOf(false)

    private var expandedDropdownIndexQueue by mutableIntStateOf(-1)
    private var expandedDropdownIndexVote by mutableIntStateOf(-1)

    fun onToggleUpvote(track: Track) {
        host.toggleUpvote(track)
    }

    fun onToggleDropdownQueue(index: Int) {
        if (expandedDropdownIndexQueue == -1) {
            expandedDropdownIndexQueue = index
        } else {
            expandedDropdownIndexQueue = -1
        }
    }

    fun isDropdownExpandedQueue(index: Int): Boolean {
        return expandedDropdownIndexQueue == index
    }

    fun onToggleDropdownVote(index: Int) {
        if (expandedDropdownIndexVote == -1) {
            expandedDropdownIndexVote = index
        } else {
            expandedDropdownIndexVote = -1
        }
    }

    fun isDropdownExpandedVote(index: Int): Boolean {
        return expandedDropdownIndexVote == index
    }

    fun onAddToQueue(track: Track) {
        //TODO
    }

    fun onRemoveFromQueue(index: Int) {
        //TODO
    }

    fun onToggleBlock(track: Track) {
        host.toggleBlockTrack(track)
    }

    fun onMoveUp(index: Int) {
        //TODO
    }

    fun onMoveDown(index: Int) {
        //TODO
    }

    fun getPausedDescription(): String {
        //TODO
        return "KP"
    }

    fun isTogglePauseAvailable(): Boolean {
        //TODO
        return false
    }

    fun onTogglePause() {
        //TODO
    }

    fun getSkipDescription(): String {
        //TODO
        return "NOONEE"
    }

    fun isSkipAvailable(): Boolean {
        //TODO
        return false
    }

    fun onSkip() {
        //TODO
    }

    fun getTrackSliderPosition(): Float {
        //TODO
        return 1f
    }

    fun onTrackSliderPositionChange(newPosition: Float) {
        //TODO
    }

    fun onTrackSliderFinish(position: Float) {
        //TODO
    }

    fun getVoteList(): SnapshotStateList<MutableState<Track>> {
        return host.voteList.value.getTracks()
    }

    fun getQueueList(): SnapshotStateList<MutableState<Track>> {
        //TODO
        return SnapshotStateList()
    }

    fun onSearchTracks(navController: NavController) {
        navController.navigate(ViewsCollection.SEARCH_VIEW.name)
    }

    fun getTopBarDescription(): String {
        return when(host.session.sessionType){
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

    fun isDeleteSessionDialogShown(): Boolean {
        return deleteSessionDialogShown
    }

    fun onBack(navController: NavController) {
        deleteSessionDialogShown = !deleteSessionDialogShown
    }

    fun onConfirmDeleteSession(navController: NavController) {
        deleteSessionDialogShown = false
        (host.backendConnector as HostBackendConnector).deleteSession()
        navController.popBackStack(
            ViewsCollection.START_VIEW.name,
            inclusive = false,
            saveState = false
        )
    }

    fun onDismissDeleteSession(navController: NavController) {
        deleteSessionDialogShown = false
    }

    fun syncState(){
        host.syncState()
    }

}