package com.example.neptune.data.model.track

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Represents a list of tracks with methods for sorting tracks by upvotes.
 * @param tracks A list of mutable states containing tracks.
 */
class VoteList(private val tracks: SnapshotStateList<MutableState<Track>>) : PlayList(tracks) {

    /**
     * Sorts the tracks in descending order based on the number of upvotes.
     */
    fun sortByUpvote() {
        tracks.sortBy { track ->  - track.value.getUpvotes() }
    }
}