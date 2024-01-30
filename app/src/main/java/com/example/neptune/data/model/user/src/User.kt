package com.example.neptune.data.model.user.src

import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.track.src.TrackList
import com.example.neptune.data.model.track.src.VoteList

open class User(val session: Session,val backendConnector: BackendConnector){
     var voteList: VoteList = VoteList()
     var blockList: TrackList = TrackList()
     var cooldownList: TrackList = TrackList()


    fun addTrackToVoteList(track : Track){
        voteList.addTrack(track)
        backendConnector.addTrackToSession(track)
    }

    fun upvoteTrack(index: Int){
        voteList.upvoteTrack(index)
        backendConnector.addUpvoteToTrack(voteList.trackAt(index))
    }
    fun removeUpvoteFromTrack(index: Int){
        voteList.removeUpvoteFromTrack(index)
        backendConnector.removeUpvoteFromTrack(voteList.trackAt(index))
    }
    open fun search(input: String): TrackList {
        var foundTracks = voteList.search(input)
        foundTracks = filterSearchResults(foundTracks)
        return TrackList(foundTracks)
    }
    protected fun filterSearchResults(searchResult: MutableList<Track>): MutableList<Track>{
        val output: MutableList<Track> = ArrayList<Track>()
        for (track in searchResult){
            if (!blockList.containsTrack(track)&& !cooldownList.containsTrack(track)&&
                session.validateTrack(track)){
                output.add(track)
            }
        }
        return output

    }
    open fun leaveSession(){
        val particantBackendConnector = backendConnector as ParticipantBackendConnector
        particantBackendConnector.participantLeaveSession()
    }
    open fun syncState(){
        val cooldownListBuilder = mutableListOf<Track>()
        val blockListBuilder = mutableListOf<Track>()
        val voteListBuilder = mutableListOf<Track>()
        backendConnector.getAllTrackData {
            for(track in it){
                if (track.isBlocked){
                    blockListBuilder.add(track)
                }
                if(track.hasCooldown){
                    cooldownListBuilder.add(track)
                }
                if(track.upvotes >0 && !track.isBlocked&& !track.isBlocked){
                    voteListBuilder.add(track)
                }
            }
            voteList = VoteList(voteListBuilder)
            blockList= TrackList(blockListBuilder)
            cooldownList = TrackList(cooldownListBuilder)
        }
    }


}