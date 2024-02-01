package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * A collection of tracks, with base functionality
 * Acts as a parent class for specialised lists of tracks
 */
open class TrackList (private val tracks: SnapshotStateList<MutableState<Track>>){

    /**
     * removes track from TrackList at index
     */
    /*fun removeTrack(index: Int){
        tracks.removeAt(index)
    }*/ //TODO probably not needed here, put in the queue

    /**
     * removes track from TrackList with matching id to the track given
     * as an input, if no track with this id is in the input nothing happens
     */
    /*fun removeTrack(track: Track){
        for (index in tracks.indices){
            if(tracks[index].value.id == track.id){
                tracks.removeAt(index)
                break
            }
        }
    }*/ //TODO probably not needed

    fun addTrack(track: MutableState<Track>){
        tracks.add(track)
    }

    fun addTrack(track: Track){
        tracks.add(mutableStateOf(track))
    }

    fun containsTrack(track: Track): Boolean{
        for (item in tracks){
            if (item.value.id == track.id){
                return true
            }
        }
        return false
    }
    fun popFirstTrack(): Track {
        var output  = tracks.first()
        tracks.removeAt(0);
        return output.value;
    }
    fun isEmpty(): Boolean{
        return tracks.isEmpty()
    }

    fun clear(){
        tracks.clear()
    }
    fun trackAt(index: Int):Track{
       return tracks[index].value
    }

    fun getTracks(): SnapshotStateList<MutableState<Track>>{
        return tracks
    }

    override fun toString(): String {
        val output= StringBuilder()

        output.append("length: ${tracks.size}\n")
        for(track in tracks){
            output.append(track.toString())
            output.append("\n")
        }
        output.append("\n\n")
        return output.toString()
    }


}