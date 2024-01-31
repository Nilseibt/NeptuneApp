package com.example.neptune.data.model.user.src

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.track.src.TrackList
import com.example.neptune.data.model.track.src.VoteList

open class User(val session: Session, val backendConnector: BackendConnector) {

    var voteList = mutableStateOf(VoteList(mutableStateListOf<MutableState<Track>>()))
    var searchList = mutableStateOf(TrackList(mutableStateListOf<MutableState<Track>>()))
    var blockList = mutableStateOf(TrackList(mutableStateListOf<MutableState<Track>>()))
    var cooldownList = mutableStateOf(TrackList(mutableStateListOf<MutableState<Track>>()))


    private var sessionTracks = HashMap<String, MutableState<Track>>()

    protected fun addOrUpdateSessionTrack(track: Track) {
        if (hasSessionTrack(track.id)) {
            getSessionTrack(track.id).value = track
        } else {
            sessionTracks[track.id] = mutableStateOf(track)
        }
    }

    protected fun hasSessionTrack(trackId: String): Boolean {
        return sessionTracks.containsKey(trackId)
    }

    protected fun getSessionTrack(trackId: String): MutableState<Track> {
        if (hasSessionTrack(trackId)) {
            return sessionTracks[trackId]!!
        } else {
            //TODO ist das sinnvoll?
            throw Exception("Track is not a session track, check first with hasSessionTrack")
        }
    }


    fun addTrackToVoteList(track: Track) {
        voteList.value.addTrack(track)
    }

    fun toggleUpvote(track: Track) {
        if (hasSessionTrack(track.id)) {
            val sessionTrack = getSessionTrack(track.id)
            if (sessionTrack.value.isUpvoted()) {
                backendConnector.removeUpvoteFromTrack(sessionTrack.value)
            } else {
                backendConnector.addUpvoteToTrack(sessionTrack.value)
            }
            sessionTrack.value.toggleUpvote()
        } else {
            if (track.isUpvoted()) {
                track.toggleUpvote()
                backendConnector.addTrackToSession(track) {
                    backendConnector.removeUpvoteFromTrack(track)
                }
            } else {
                track.toggleUpvote()
                backendConnector.addTrackToSession(track) {
                    backendConnector.addUpvoteToTrack(track)
                }
            }
            addOrUpdateSessionTrack(track)
        }
    }

    open fun search(input: String) {
        //TODO write it again, mutableStates broke the method
        var foundTracks = voteList.value.search(input)
        foundTracks = filterSearchResults(foundTracks)
        //searchList.value = TrackList(foundTracks)
    }

    protected fun filterSearchResults(searchResult: MutableList<Track>): MutableList<Track> {
        val output: MutableList<Track> = ArrayList<Track>()
        for (track in searchResult) {
            if (!blockList.value.containsTrack(track) && !cooldownList.value.containsTrack(track) &&
                session.validateTrack(track)
            ) {
                output.add(track)
            }
        }
        return output

    }

    open fun leaveSession() {

    }


    fun syncTracksFromBackend() {
        backendConnector.getAllTrackData { listOfTracks ->
            listOfTracks.forEach { track ->
                addOrUpdateSessionTrack(track)
            }
            updateVoteList()
        }
    }

    fun updateVoteList() {
        val updatedVoteList = VoteList(mutableStateListOf())
        sessionTracks.forEach { (trackId, track) ->
            if(track.value.getUpvotes() > 0){
                updatedVoteList.addTrack(track)
            }
        }
        updatedVoteList.sortByUpvote()
        voteList.value = updatedVoteList
    }


}