package com.example.neptune.data.model.user

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.track.Track
import com.example.neptune.data.model.track.TrackList
import com.example.neptune.data.model.track.VoteList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Represents a user within a session with functionalities such as upvoting, searching, and managing track lists.
 * @param session The session associated with the user.
 * @param backendConnector The backend connector for the user.
 * @param upvoteDatabase The upvote database for the user.
 */
open class User(
    val session: Session,
    val backendConnector: BackendConnector,
    private val upvoteDatabase: UpvoteDatabase
) {

    /**
     * The list of tracks that have been upvoted by users in the session.
     */
    var voteList = mutableStateOf(VoteList(mutableStateListOf()))
    /**
     * The list of tracks obtained from a search operation.
     */
    var searchList = mutableStateOf(TrackList(mutableStateListOf()))
    /**
     * The list of tracks that have been blocked by the host.
     */
    var blockList = mutableStateOf(TrackList(mutableStateListOf()))
    /**
     * The list of tracks that are on a cooldown period.
     */
    var cooldownList = mutableStateOf(TrackList(mutableStateListOf()))


    private var sessionTracks = HashMap<String, MutableState<Track>>()

    private var upvotedTrackIdsInSession = mutableListOf<String>()

    init {
        GlobalScope.launch {
            upvotedTrackIdsInSession = upvoteDatabase.getUpvotedTrackIds(session).toMutableList()
            addUpvotesToSessionTracks()
        }
    }

    /**
     * Adds or updates the specified track within the session.
     * @param track The track to be added or updated.
     */
    protected fun addOrUpdateSessionTrack(track: Track) {
        if (hasSessionTrack(track.id)) {
            getSessionTrack(track.id).value = track
        } else {
            sessionTracks[track.id] = mutableStateOf(track)
        }
    }

    /**
     * Checks if the specified track is within the session.
     * @param trackId The ID of the track to check.
     * @return True if the track is within the session, false otherwise.
     */
    protected fun hasSessionTrack(trackId: String): Boolean {
        return sessionTracks.containsKey(trackId)
    }

    /**
     * Retrieves the track with the specified ID from the session.
     * @param trackId The ID of the track to retrieve.
     * @return The mutable state of the track.
     * @throws Exception if the track is not found in the session.
     */
    protected fun getSessionTrack(trackId: String): MutableState<Track> {
        if (hasSessionTrack(trackId)) {
            return sessionTracks[trackId]!!
        } else {
            throw Exception("Track is not a session track, check first with hasSessionTrack")
        }
    }

    /**
     * Toggles the upvote status of the specified track.
     * @param track The track to toggle upvote status.
     */
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

    /**
     * Searches for tracks based on the provided input query.
     * @param input The search query.
     */
    open fun search(input: String) {
        var foundTracks = voteList.value.search(input)
        searchList.value = TrackList(foundTracks.toMutableStateList())
    }

    /**
     * Requests statistics from the backend and invokes the provided callback with the statistics.
     * @param callback The callback function to handle the statistics.
     */
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


    /**
     * Synchronizes the user's state with the backend by updating tracks and track lists.
     */
    open fun syncState() {
        syncTracksFromBackend()
    }

    /**
     * Syncs tracks from the backend and updates track lists accordingly.
     */
    protected fun syncTracksFromBackend() {
        backendConnector.getAllTrackData() { listOfTracks ->
            if(session.hasAddedTrack && listOfTracks.isEmpty()){
                session.isSessionClosed = true
            }
            if(!session.hasAddedTrack && listOfTracks.isNotEmpty()){
                session.hasAddedTrack = true
            }
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
            if (track.value.getUpvotes() > 0 || session.sessionType == SessionType.PLAYLIST) {
                updatedVoteList.addTrack(track)
            }
            if(track.value.isBlocked()){
                updatedBlockList.addTrack(track)
            }
            if(track.value.hasCooldown()){
                updatedCooldownList.addTrack(track)
            }
            for(searchListIndex in 0 until searchList.value.getTracks().size){
                if(searchList.value.trackAt(searchListIndex).id == trackId){
                    searchList.value.exchangeTrack(searchListIndex, track)
                }
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