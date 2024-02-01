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
        host.addTrackToQueue(track)
    }

    fun onRemoveFromQueue(index: Int) {
        host.removeTrackFromQueue(index)
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

    fun getPausedDescription(): String {
        return when(host.getPlaybackState().value){
            PlaybackState.INITIAL -> ""
            PlaybackState.PAUSED -> "Weiter"
            PlaybackState.PLAYING -> "Pause"
        }
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

    fun getSkipDescription(): String {
        //TODO
        return "NOONEE"
    }

    fun isSkipAvailable(): Boolean {
        //TODO
        return false
    }

    fun onSkip() {
        host.skip()
    }

    fun getTrackSliderPosition(): Float {
        return host.getPlayProgress().value
    }

    fun onTrackSliderPositionChange(newPosition: Float) {
        host.changePlayProgressWithoutSpotify(newPosition)
    }

    fun onTrackSliderFinish() {
        host.setPlayProgressToSpotify()
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