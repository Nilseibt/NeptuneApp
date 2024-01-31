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


    fun addTrackToVoteList(track: Track) {
        voteList.value.addTrack(track)
    }

    fun upvoteTrack(index: Int) {
        voteList.value.upvoteTrack(index)
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


}