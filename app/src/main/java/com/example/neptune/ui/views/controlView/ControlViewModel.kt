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
import com.example.neptune.data.model.streamingConnector.spotifyConnector.PlaybackState
import com.example.neptune.data.model.track.Track
import com.example.neptune.data.model.user.Host
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
        host.addTrackToQueue(track)
        expandedDropdownIndexVote = -1
    }

    fun onRemoveFromQueue(index: Int) {
        host.removeTrackFromQueue(index)
        expandedDropdownIndexQueue = -1
    }

    fun onToggleBlock(track: Track) {
        host.toggleBlockTrack(track)
    }

    fun onMoveUp(index: Int) {
        host.moveTrackUpInQueue(index)
    }

    fun onMoveDown(index: Int) {
        host.moveTrackDownInQueue(index)
    }

    fun isPaused(): Boolean {
        return host.getPlaybackState().value != PlaybackState.PLAYING
    }

    fun isTogglePauseAvailable(): Boolean {
        return host.getPlaybackState().value != PlaybackState.INITIAL
    }

    fun onTogglePause() {
        if(host.getPlaybackState().value == PlaybackState.PLAYING) {
            host.pausePlay()
        } else if(host.getPlaybackState().value == PlaybackState.PAUSED) {
            host.resumePlay()
        }
    }

    fun isSkipAvailable(): Boolean {
        return host.isSkipAvailable()
    }

    fun onSkip() {
        host.skip()
    }

    fun getTrackSliderPosition(): Float {
        return host.getPlayProgress()
    }

    fun onTrackSliderPositionChange(newPosition: Float) {
        host.dragPlayProgress(newPosition)
    }

    fun onTrackSliderFinish() {
        host.finishDragPlayProgress()
    }

    fun getVoteList(): SnapshotStateList<MutableState<Track>> {
        return host.voteList.value.getTracks()
    }

    fun getQueueList(): SnapshotStateList<MutableState<Track>> {
        return host.queue.value.getTracks()
    }

    fun onSearchTracks(navController: NavController) {
        navController.navigate(ViewsCollection.SEARCH_VIEW.name)
    }

    fun getTopBarDescription(): SessionType {
        return host.session.sessionType
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
        if(!navController.popBackStack(
            ViewsCollection.START_VIEW.name,
            inclusive = false,
            saveState = false
        )){
            navController.navigate(ViewsCollection.START_VIEW.name)
        }
    }

    fun onDismissDeleteSession(navController: NavController) {
        deleteSessionDialogShown = false
    }

    fun syncState(){
        host.syncState()
    }

}