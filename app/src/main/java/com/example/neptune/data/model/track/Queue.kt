package com.example.neptune.data.model.track

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList


/**
 * Represents a queue of tracks.
 * @param tracks The list of tracks in the queue.
 */
class Queue(private val tracks: SnapshotStateList<MutableState<Track>>) : TrackList(tracks) {

    /**
     * Moves the track at the specified index down by one position in the queue.
     * @param index The index of the track to be moved down.
     */
    fun moveTrackDown(index: Int) {
        if (index > 0 && index < tracks.size - 1) {
            val temporary = tracks[index]
            tracks[index] = tracks[index + 1]
            tracks[index + 1] = temporary
        }
    }

    /**
     * Moves the track at the specified index up by one position in the queue.
     * @param index The index of the track to be moved up.
     */
    fun moveTrackUp(index: Int) {
        if (index > 1) {
            val temporary = tracks[index]
            tracks[index] = tracks[index - 1]
            tracks[index - 1] = temporary
        }
    }

    /**
     * Removes the track at the specified index from the queue.
     * @param index The index of the track to be removed.
     */
    fun removeTrack(index: Int) {
        if (index >= 0 && index < tracks.size) {
            tracks.removeAt(index)
        }
    }
}