package com.example.neptune.data.model.track.src

open class PlayList(tracks: MutableList<Track>): TrackList(tracks) {
    constructor():this(ArrayList<Track>()){}
    fun search(input :String): MutableList<Track> {
        val output =  ArrayList<Track>()
        for( track in tracks){
            if(track.name.contains(input)){
                output.add(track)
            }
        }
        tracks.sortBy { track -> track.name.length }
        return output;
    }
}