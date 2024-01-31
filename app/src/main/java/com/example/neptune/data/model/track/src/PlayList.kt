package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

open class PlayList(private val tracks: SnapshotStateList<MutableState<Track>>): TrackList(tracks) {
    fun search(input :String): MutableList<Track> {
        val output =  ArrayList<Track>()
        for( track in tracks){
            if(track.value.name.contains(input,ignoreCase = true)){
                output.add(track.value)
            }
        }
        tracks.sortBy { track -> track.value.name.length }
        return output;
    }
}