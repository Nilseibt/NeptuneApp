package com.example.neptune.model.track.src

open class PlayList(tracks: MutableList<Track>): TrackList(tracks) {

    fun search(input :String): TrackList{
        //Todo implement search algorithm
        return TrackList(ArrayList());
    }
}