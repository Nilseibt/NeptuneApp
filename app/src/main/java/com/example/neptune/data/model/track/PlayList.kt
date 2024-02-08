package com.example.neptune.data.model.track

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Represents a playlist containing tracks.
 * @param tracks The list of tracks in the playlist.
 */
open class PlayList(
    private val tracks: SnapshotStateList<MutableState<Track>>
) : TrackList(tracks) {

    /**
     * Searches for tracks in the playlist that match the given input.
     * @param input The search input.
     * @return A list of tracks whose names contain the search input (case-insensitive).
     */
    fun search(input: String): MutableList<MutableState<Track>> {

        val foundTracks = mutableListOf<MutableState<Track>>()
        for (track in tracks) {
            if (track.value.name.contains(input, ignoreCase = true)) {
                foundTracks.add(track)
            }
        }
        tracks.sortBy { track -> track.value.name.length }
        return foundTracks;
    }
}