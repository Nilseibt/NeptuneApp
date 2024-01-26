package com.example.neptune.data.model.track.src

class VoteList(tracks: MutableList<Track>) : PlayList(tracks) {

    constructor():this(ArrayList<Track>()){}

    fun upvoteTrack(index: Int) {
        tracks[index].addUpvote()
    }

    fun removeUpvoteFromTrack(index: Int) {
        tracks[index].removeUpvote()
    }

    fun sortByUpvote() {
        tracks.sortBy { track -> track.upvotes }
    }
}