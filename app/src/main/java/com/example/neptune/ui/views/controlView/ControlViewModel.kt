package com.example.neptune.ui.views.controlView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.model.track.src.Track

class ControlViewModel() : ViewModel() {

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
        //TODO
        return SnapshotStateList()
    }

    fun getQueueList(): SnapshotStateList<MutableState<Track>> {
        //TODO
        return SnapshotStateList()
    }

    fun onSearchTracks(navController: NavController) {
        //TODO
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

    fun onBack(navController: NavController) {
        //TODO
    }

    fun onConfirmDeleteSession(navController: NavController) {
        //TODO
    }

    fun onDismissDeleteSession(navController: NavController) {
        //TODO
    }

}