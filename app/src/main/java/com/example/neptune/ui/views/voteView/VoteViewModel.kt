package com.example.neptune.ui.views.voteView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.model.track.src.Track

class VoteViewModel() : ViewModel() {


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

    fun getVoteList(): SnapshotStateList<MutableState<Track>> {
        //TODO
        return SnapshotStateList()
    }

    fun onSearchTracks(navController: NavController) {
        //TODO
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


    fun onConfirmLeaveSession(navController: NavController) {
        //TODO
    }


    fun onDismissLeaveSession(navController: NavController) {
        //TODO
    }

}