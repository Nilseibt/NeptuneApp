package com.example.neptune.model.track.src

open class TrackList (val tracks: MutableList<Track>){

    fun removeTrack(index: Int){
            tracks.removeAt(index)
    }
    fun addTrack(track: Track){
        tracks.add(track)
    }
    fun containsTrack(track: Track): Boolean{
        for (item in tracks){
            if (item.id == track.id){
                return true
            }
        }
        return false
    }
    fun popFirstTrack(): Track {
        var output  = tracks.first()
        tracks.removeAt(0);
        return output;
    }
    fun isEmpty(): Boolean{
        return tracks.isEmpty()
    }

}