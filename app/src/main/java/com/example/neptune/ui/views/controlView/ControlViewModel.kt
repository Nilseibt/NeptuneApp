package com.example.neptune.ui.views.controlView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.user.src.Host
import com.example.neptune.ui.views.ViewsCollection

class ControlViewModel(
    val host: Host
) : ViewModel() {

    private var deleteSessionDialogShown by mutableStateOf(false)

    fun onToggleUpvote(track: Track) {
        //TODO
    }

    fun onToggleDropdown(index: Int) {
        //TODO
    }

    fun isDropdownExpanded(index: Int): Boolean {
        //TODO
        return false
    }

    fun onAddToQueue(track: Track) {
        //TODO
    }

    fun onRemoveFromQueue(index: Int) {
        //TODO
    }

    fun onToggleBlock(track: Track) {
        //TODO
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
        return host.voteList.value.getListOfTracks()
    }

    fun getQueueList(): SnapshotStateList<MutableState<Track>> {
        //TODO
        return SnapshotStateList()
    }

    fun onSearchTracks(navController: NavController) {
        navController.navigate(ViewsCollection.SEARCH_VIEW.name)
    }

    fun getTopBarDescription(): String {
        //TODO
        return "TOPBAR"
    }

    fun onOpenInfo(navController: NavController) {
        //TODO
    }

    fun onOpenStats(navController: NavController) {
        //TODO
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

    fun syncTracksFromBackend(){
        host.syncTracksFromBackend()
    }

}