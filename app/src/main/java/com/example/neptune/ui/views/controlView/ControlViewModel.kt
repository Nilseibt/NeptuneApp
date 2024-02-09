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


/**
 * ViewModel class for controlling the logic of the ControlView.
 *
 * @property host The host of the current session.
 */
class ControlViewModel(
    val host: Host
) : ViewModel() {

    private var deleteSessionDialogShown by mutableStateOf(false)

    private var expandedDropdownIndexQueue by mutableIntStateOf(-1)
    private var expandedDropdownIndexVote by mutableIntStateOf(-1)

    /**
     * Toggles the upvote state of a track.
     *
     * @param track The track to toggle the upvote for.
     */
    fun onToggleUpvote(track: Track) {
        host.toggleUpvote(track)
    }

    /**
     * Toggles the dropdown state for the queue for the track at the specified index.
     *
     * @param index The index of the track in the queue.
     */
    fun onToggleDropdownQueue(index: Int) {
        if (expandedDropdownIndexQueue == -1) {
            expandedDropdownIndexQueue = index
        } else {
            expandedDropdownIndexQueue = -1
        }
    }

    /**
     * Checks if the dropdown is expanded for the track in the queue at the specified index.
     *
     * @param index The index of the track in the queue.
     * @return True if the dropdown is expanded, false otherwise.
     */
    fun isDropdownExpandedQueue(index: Int): Boolean {
        return expandedDropdownIndexQueue == index
    }

    /**
     * Toggles the dropdown state for the vote list for the track at the specified index.
     *
     * @param index The index of the track in the vote list.
     */
    fun onToggleDropdownVote(index: Int) {
        if (expandedDropdownIndexVote == -1) {
            expandedDropdownIndexVote = index
        } else {
            expandedDropdownIndexVote = -1
        }
    }

    /**
     * Checks if the dropdown is expanded for the track in the vote list at the specified index.
     *
     * @param index The index of the track in the vote list.
     * @return True if the dropdown menu is expanded for the given track, false otherwise.
     */
    fun isDropdownExpandedVote(index: Int): Boolean {
        return expandedDropdownIndexVote == index
    }

    /**
     * Adds a track to the queue.
     *
     * @param track The track to be added to the queue.
     */
    fun onAddToQueue(track: Track) {
        expandedDropdownIndexVote = -1
        host.addTrackToQueue(track)
    }

    /**
     * Removes a track from the queue.
     *
     * @param index The index of the track in the queue.
     */
    fun onRemoveFromQueue(index: Int) {
        expandedDropdownIndexQueue = -1
        host.removeTrackFromQueue(index)
    }

    /**
     * Toggles the block status of a track.
     *
     * @param track The track to toggle the block status for.
     */
    fun onToggleBlock(track: Track) {
        expandedDropdownIndexQueue = -1
        expandedDropdownIndexVote = -1
        host.toggleBlockTrack(track)
    }

    /**
     * Moves a track up in the queue if possible.
     *
     * @param index The index of the track to be moved up.
     */
    fun onMoveUp(index: Int) {
        host.moveTrackUpInQueue(index)
    }

    /**
     * Moves a track down in the queue if possible.
     *
     * @param index The index of the track to be moved down.
     */
    fun onMoveDown(index: Int) {
        host.moveTrackDownInQueue(index)
    }

    /**
     * Checks if the playback is currently paused.
     *
     * @return True if playback is paused, false otherwise.
     */
    fun isPaused(): Boolean {
        return host.getPlaybackState().value != PlaybackState.PLAYING
    }

    /**
     * Checks if toggling pause is available.
     *
     * @return True if pause can be toggled, false otherwise.
     */
    fun isTogglePauseAvailable(): Boolean {
        return host.getPlaybackState().value != PlaybackState.INITIAL
    }

    /**
     * Toggles the playback state between play and pause.
     */
    fun onTogglePause() {
        if (host.getPlaybackState().value == PlaybackState.PLAYING) {
            host.pausePlay()
        } else if (host.getPlaybackState().value == PlaybackState.PAUSED) {
            host.resumePlay()
        }
    }

    /**
     * Checks if skipping to the next track is available.
     *
     * @return True if skipping is available, false otherwise.
     */
    fun isSkipAvailable(): Boolean {
        return host.isSkipAvailable()
    }

    /**
     * Skips to the next track.
     */
    fun onSkip() {
        host.skip()
    }

    /**
     * Retrieves the current position of the track slider.
     *
     * @return The current position of the track slider from 0 to 1.
     */
    fun getTrackSliderPosition(): Float {
        return host.getPlayProgress()
    }

    /**
     * Handles the change in position of the track slider.
     *
     * @param newPosition The new position of the track slider from 0 to 1.
     */
    fun onTrackSliderPositionChange(newPosition: Float) {
        host.dragPlayProgress(newPosition)
    }

    /**
     * Handles the finish event of the track slider movement.
     */
    fun onTrackSliderFinish() {
        host.finishDragPlayProgress()
    }

    /**
     * Retrieves the list of tracks from the vote list.
     *
     * @return The list of tracks in the vote list.
     */
    fun getVoteList(): SnapshotStateList<MutableState<Track>> {
        return host.voteList.value.getTracks()
    }

    /**
     * Retrieves the list of tracks from the queue.
     *
     * @return The list of tracks in the queue.
     */
    fun getQueueList(): SnapshotStateList<MutableState<Track>> {
        return host.queue.value.getTracks()
    }

    /**
     * Navigates to the search view.
     *
     * @param navController The navigation controller to perform the navigation.
     */
    fun onSearchTracks(navController: NavController) {
        navController.navigate(ViewsCollection.SEARCH_VIEW.name)
    }

    /**
     * Retrieves the session type for the top bar description.
     *
     * @return The session type.
     */
    fun getTopBarDescription(): SessionType {
        return host.session.sessionType
    }

    /**
     * Navigates to the info view.
     *
     * @param navController The navigation controller to perform the navigation.
     */
    fun onOpenInfo(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }

    /**
     * Navigates to the stats view.
     *
     * @param navController The navigation controller to perform the navigation.
     */
    fun onOpenStats(navController: NavController) {
        navController.navigate(ViewsCollection.STATS_VIEW.name)
    }

    /**
     * Checks if the delete session dialog is shown.
     *
     * @return True if the dialog is shown, false otherwise.
     */
    fun isDeleteSessionDialogShown(): Boolean {
        return deleteSessionDialogShown
    }

    /**
     * Handles the back action by toggling the delete session dialog.
     *
     * @param navController The navigation controller to perform the navigation.
     */
    fun onBack(navController: NavController) {
        deleteSessionDialogShown = !deleteSessionDialogShown
    }

    /**
     * Confirms the deletion of the session.
     *
     * @param navController The navigation controller to perform the navigation.
     */
    fun onConfirmDeleteSession(navController: NavController) {
        deleteSessionDialogShown = false
        (host.backendConnector as HostBackendConnector).deleteSession()
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
     * Dismisses the delete session dialog.
     *
     * @param navController The navigation controller to perform the navigation.
     */
    fun onDismissDeleteSession(navController: NavController) {
        deleteSessionDialogShown = false
    }

    /**
     * Synchronizes the state with the backend and the streaming service.
     */
    fun syncState() {
        host.syncState()
    }

    /**
     * Checks if the streaming hint is available.
     *
     * @return True if the streaming hint is available, false otherwise.
     */
    fun isStreamingHintAvailable(): Boolean {
        return host.getStreamingHint().value != ""
    }

    /**
     * Retrieves the streaming hint.
     *
     * @return The streaming hint.
     */
    fun getStreamingHint(): String {
        return host.getStreamingHint().value
    }

}