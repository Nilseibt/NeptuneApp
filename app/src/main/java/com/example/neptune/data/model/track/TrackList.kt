package com.example.neptune.data.model.track

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Represents a list of tracks with methods to manipulate the list such as adding, removing, and retrieving tracks.
 * @param tracks A list of mutable states containing tracks.
 */
open class TrackList(private val tracks: SnapshotStateList<MutableState<Track>>) {


    /**
     * Removes the specified track from the list.
     * @param track The track to be removed.
     */
    fun removeTrack(track: Track) {
        for (index in tracks.indices) {
            if (tracks[index].value.id == track.id) {
                tracks.removeAt(index)
                break
            }
        }
    }

    /**
     * Adds a track to the list.
     * @param track The mutable state containing the track to be added.
     */
    fun addTrack(track: MutableState<Track>) {
        tracks.add(track)
    }

    /**
     * Adds a track to the list.
     * @param track The track to be added.
     */
    fun addTrack(track: Track) {
        tracks.add(mutableStateOf(track))
    }

    /**
     * Replaces the track at the specified index with the given track.
     * @param index The index of the track to be replaced.
     * @param track The new track.
     */
    fun exchangeTrack(index: Int, track: MutableState<Track>) {
        tracks[index] = track
    }

    /**
     * Checks if the list contains the specified track.
     * @param track The track to be checked.
     * @return True if the track is found, false otherwise.
     */
    fun containsTrack(track: Track): Boolean {
        for (item in tracks) {
            if (item.value.id == track.id) {
                return true
            }
        }
        return false
    }

    /**
     * Removes and returns the first track from the list.
     * @return The first track in the list.
     * @throws ArrayIndexOutOfBoundsException if the list is empty.
     */
    fun popFirstTrack(): Track {
        if (tracks.size > 0) {
            var output = tracks.first()
            tracks.removeAt(0)
            return output.value
        } else {
            throw ArrayIndexOutOfBoundsException("No track in Queue, cannot pop first track")
        }
    }

    /**
     * Checks if the list is empty.
     * @return True if the list is empty, false otherwise.
     */
    fun isEmpty(): Boolean {
        return tracks.isEmpty()
    }

    /**
     * Clears all tracks from the list.
     */
    fun clear() {
        tracks.clear()
    }

    /**
     * Retrieves the track at the specified index in the list.
     * @param index The index of the track to be retrieved.
     * @return The track at the specified index.
     */
    fun trackAt(index: Int): Track {
        return tracks[index].value
    }

    /**
     * Retrieves the list of tracks.
     * @return The list of tracks.
     */
    fun getTracks(): SnapshotStateList<MutableState<Track>> {
        return tracks
    }

    /**
     * Provides a string representation of the track list including its length and each track.
     * @return A formatted string representing the track list.
     */
    override fun toString(): String {
        val output = StringBuilder()

        output.append("length: ${tracks.size}\n")
        for (track in tracks) {
            output.append(track.toString())
            output.append("\n")
        }
        output.append("\n\n")
        return output.toString()
    }


}












