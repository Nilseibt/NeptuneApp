package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

class VoteList(private val tracks: SnapshotStateList<MutableState<Track>>) : PlayList(tracks) {

    /*fun upvoteTrack(index: Int) {
        tracks[index].value.addUpvote()
    }

    fun removeUpvoteFromTrack(index: Int) {
        tracks[index].value.removeUpvote()
    }*/

    fun sortByUpvote() {
        tracks.sortBy { track -> track.value.getUpvotes() }
    }
}