package com.example.neptune.model.track.src

class Queue( tracks : MutableList<Track>) : TrackList(tracks) {

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