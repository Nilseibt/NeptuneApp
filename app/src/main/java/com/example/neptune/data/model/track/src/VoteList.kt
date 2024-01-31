package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

class VoteList(private val tracks: SnapshotStateList<MutableState<Track>>) : PlayList(tracks) {
    constructor():this(SnapshotStateList<MutableState<Track>>()){}

    fun sortByUpvote() {
        tracks.sortBy { track -> track.value.getUpvotes() }
        tracks.reverse()
    }
}