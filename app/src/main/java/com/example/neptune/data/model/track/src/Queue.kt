package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

class Queue(private val tracks : SnapshotStateList<MutableState<Track>>) : TrackList(tracks) {
    constructor():this(SnapshotStateList<MutableState<Track>>()){}

    fun moveTrackDown(index: Int){
        if (tracks.size > index -1){
            //swap with track below index position
            val tmp = tracks[index]
            tracks[index] = tracks[index+1]
            tracks[index+1] = tmp
        }
    }
    fun moveTrackUp(index: Int){
        if (index > 0){
            //swap with track above index position
            val tmp = tracks[index]
            tracks[index] = tracks[index-1]
            tracks[index-1] = tmp
        }
    }
}