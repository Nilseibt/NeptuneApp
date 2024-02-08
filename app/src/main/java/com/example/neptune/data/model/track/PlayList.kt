package com.example.neptune.data.model.track

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

open class PlayList(
    private val tracks: SnapshotStateList<MutableState<Track>>
): TrackList(tracks) {

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