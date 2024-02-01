package com.example.neptune.data.model.user.src

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.track.src.TrackList
import com.example.neptune.data.model.track.src.VoteList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class User(
    val session: Session,
    val backendConnector: BackendConnector,
    private val upvoteDatabase: UpvoteDatabase
) {

    var voteList = mutableStateOf(VoteList(mutableStateListOf<MutableState<Track>>()))
    var searchList = mutableStateOf(TrackList(mutableStateListOf<MutableState<Track>>()))
    var blockList = mutableStateOf(TrackList(mutableStateListOf<MutableState<Track>>()))
    var cooldownList = mutableStateOf(TrackList(mutableStateListOf<MutableState<Track>>()))


    private var sessionTracks = HashMap<String, MutableState<Track>>()

    private var upvotedTrackIdsInSession = mutableListOf<String>()

    init {
        GlobalScope.launch {
            upvotedTrackIdsInSession = upvoteDatabase.getUpvotedTrackIds(session).toMutableList()
            addUpvotesToSessionTracks()
        }
    }

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

    fun toggleUpvote(track: Track) {
        if (hasSessionTrack(track.id)) {
            val sessionTrack = getSessionTrack(track.id)
            if (sessionTrack.value.isUpvoted()) {
                backendConnector.removeUpvoteFromTrack(sessionTrack.value)
                upvotedTrackIdsInSession.remove(track.id)
                GlobalScope.launch {
                    upvoteDatabase.removeUpvote(session, track.id)
                }
            } else {
                backendConnector.addUpvoteToTrack(sessionTrack.value)
                upvotedTrackIdsInSession.add(track.id)
                GlobalScope.launch {
                    upvoteDatabase.addUpvote(session, track.id)
                }
            }
            sessionTrack.value.toggleUpvote()
        } else {
            if (track.isUpvoted()) {
                track.toggleUpvote()
                backendConnector.addTrackToSession(track) {
                    backendConnector.removeUpvoteFromTrack(track)
                    upvotedTrackIdsInSession.remove(track.id)
                    GlobalScope.launch {
                        upvoteDatabase.removeUpvote(session, track.id)
                    }
                }
            } else {
                track.toggleUpvote()
                backendConnector.addTrackToSession(track) {
                    backendConnector.addUpvoteToTrack(track)
                    upvotedTrackIdsInSession.add(track.id)
                    GlobalScope.launch {
                        upvoteDatabase.addUpvote(session, track.id)
                    }
                }
            }
            addOrUpdateSessionTrack(track)
        }
    }

    open fun search(input: String) {
        var foundTracks = voteList.value.search(input)
        searchList.value = TrackList(foundTracks.toMutableStateList())
    }

    fun requestStatistics(
        callback: (
        mostUpvotedSong: String,
        mostUpvotedGenre: String,
        mostUpvotedArtist: String,
        totalPlayedTracks: Int,
        sessionDuration: String,
        totalParticipants: Int,
        totalUpvotes: Int
    ) -> Unit
    ){
        backendConnector.getStatistics(callback)
    }

    protected fun filterSearchResults(searchResult: MutableList<Track>): MutableList<Track> {
        //TODO right now unused
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
        //TODO right now unused
        val particantBackendConnector = backendConnector as ParticipantBackendConnector
        particantBackendConnector.participantLeaveSession()
    }

    open fun syncState() {
        syncTracksFromBackend()
    }

    protected fun syncTracksFromBackend() {
        backendConnector.getAllTrackData() { listOfTracks ->
            listOfTracks.forEach { track ->
                if (upvotedTrackIdsInSession.contains(track.id)) {
                    track.setUpvoted(true)
                }
                if(track.getUpvotes() == 0 && upvotedTrackIdsInSession.contains(track.id)){
                    GlobalScope.launch {
                        upvoteDatabase.removeUpvote(session, track.id)
                    }
                    upvotedTrackIdsInSession.remove(track.id)
                }
                addOrUpdateSessionTrack(track)
            }
            updateTrackLists()
        }
    }

    private fun updateTrackLists() {
        val updatedVoteList = VoteList(mutableStateListOf())
        val updatedBlockList = TrackList(mutableStateListOf())
        val updatedCooldownList = TrackList(mutableStateListOf())
        sessionTracks.forEach { (trackId, track) ->
            if (track.value.getUpvotes() > 0) {
                updatedVoteList.addTrack(track)
            }
            if(track.value.isBlocked()){
                updatedBlockList.addTrack(track)
            }
            if(track.value.hasCooldown()){
                updatedCooldownList.addTrack(track)
            }
        }
        updatedVoteList.sortByUpvote()
        voteList.value = updatedVoteList
        blockList.value = updatedBlockList
        cooldownList.value = updatedCooldownList
    }


    private fun addUpvotesToSessionTracks() {
        sessionTracks.forEach { (trackId, track) ->
            if (upvotedTrackIdsInSession.contains(trackId)) {
                track.value.setUpvoted(true)
            }
        }
    }


}