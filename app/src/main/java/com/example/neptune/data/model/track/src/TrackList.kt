package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

open class TrackList(private val tracks: SnapshotStateList<MutableState<Track>>) {

    fun removeTrack(index: Int) {
        tracks.removeAt(index)
    }

    fun addTrack(track: Track) {
        tracks.add(mutableStateOf(track))
    }

    fun addTrack(track: MutableState<Track>) {
        tracks.add(track)
    }

    fun containsTrack(track: Track): Boolean {
        for (item in tracks) {
            if (item.value.id == track.id) {
                return true
            }
        }
        return false
    }

    fun popFirstTrack(): Track {
        var output = tracks.first()
        tracks.removeAt(0);
        return output.value;
    }

    fun isEmpty(): Boolean {
        return tracks.isEmpty()
    }

    fun clear() {
        tracks.clear()
    }

    fun trackAt(index: Int): Track {
        return tracks[index].value
    }

    fun getListOfTracks(): SnapshotStateList<MutableState<Track>> {
        return tracks
    }

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