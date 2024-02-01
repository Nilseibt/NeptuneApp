package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

class Queue(private val tracks : SnapshotStateList<MutableState<Track>>) : TrackList(tracks) {

    fun moveTrackDown(index: Int){
        if(index < tracks.size - 1){
            val temporary = tracks[index]
            tracks[index] = tracks[index + 1]
            tracks[index + 1] = temporary
        }
    }

    fun moveTrackUp(index: Int){
        if(index > 0){
            val temporary = tracks[index]
            tracks[index] = tracks[index - 1]
            tracks[index - 1] = temporary
        }
    }

    fun removeTrack(index: Int){
        if(index >= 0 && index < tracks.size){
            tracks.removeAt(index)
        }
    }
}