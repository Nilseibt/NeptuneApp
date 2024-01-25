package com.example.neptune.ui.views.searchView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.model.track.src.Track
import com.example.neptune.ui.commons.TrackListType
import com.example.neptune.ui.views.ViewsCollection

class SearchViewModel() : ViewModel() {

    private var searchInput by mutableStateOf("")

    fun getSearchTrackListType(): TrackListType {
        /*if (user is Host) {
            return TrackListType.HOST_SEARCH
        } else {
            return TrackListType.PARTICIPANT_SEARCH
        }*/
        return TrackListType.PARTICIPANT_SEARCH // For now //TODO
    }

    fun getTrackSearchInput(): String {
        return searchInput
    }

    fun onTrackSearchInputChange(newInput: String) {
        searchInput = newInput
        //TODO
    }

    fun isSearchButtonActive(): Boolean {
        //TODO
        return false
    }

    fun onSearchButtonClick() {
        //TODO
    }

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

    fun onToggleBlock(track: Track) {
        //TODO
    }

    fun getSearchList(): SnapshotStateList<MutableState<Track>> {
        //TODO
        return SnapshotStateList()
    }

    fun getActiveFilter(): String {
        //TODO
        return "NONE"
    }

    fun onSetActiveFilter(filter: String) {
        //TODO
    }

    fun getCooldownTracks(): SnapshotStateList<MutableState<Track>> {
        //TODO
        return SnapshotStateList()
    }

    fun getBlockedTracks(): SnapshotStateList<MutableState<Track>> {
        //TODO
        return SnapshotStateList()
    }

    fun getTopBarDescription(): String {
        //TODO
        return "Topbar"
    }

    fun onOpenInfo(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }


    fun onOpenStats(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }


    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}