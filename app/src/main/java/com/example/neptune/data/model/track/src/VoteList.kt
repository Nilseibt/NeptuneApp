package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

class VoteList(private val tracks: SnapshotStateList<MutableState<Track>>) : PlayList(tracks) {

    fun sortByUpvote() {
        tracks.sortBy { track -> 65000 - track.value.getUpvotes() }
    }
}