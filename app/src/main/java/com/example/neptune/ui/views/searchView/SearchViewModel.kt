package com.example.neptune.ui.views.searchView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.model.track.src.Track

class SearchViewModel() : ViewModel() {


    fun getTrackSearchInput(): String {
        //TODO
        return "NO SEARCH"
    }

    fun onTrackSearchInputChange(newInput: String) {
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
        //TODO
    }


    fun onOpenStats(navController: NavController) {
        //TODO
    }


    fun onBack(navController: NavController) {
        //TODO
    }

}