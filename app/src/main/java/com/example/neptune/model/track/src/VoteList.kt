package com.example.neptune.model.track.src

class VoteList(tracks: MutableList<Track>): PlayList(tracks) {

    fun upvoteTrack(index: Int){
        tracks[index].addUpvote()
    }
    fun removeUpvoteFromTrack(index: Int){
        tracks[index].removeUpvote()
    }
    fun sortByUpvote(){
        tracks.sortBy { track ->  track.upvotes }
    }
}