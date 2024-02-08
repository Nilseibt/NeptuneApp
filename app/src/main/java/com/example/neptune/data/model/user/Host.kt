package com.example.neptune.data.model.user


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.PlaybackState

import com.example.neptune.data.model.track.Queue
import com.example.neptune.data.model.track.Track


class Host(
    session: Session,
    hostBackendConnector: HostBackendConnector,
    hostStreamingConnector: HostStreamingConnector,
    upvoteDatabase: UpvoteDatabase
) : FullParticipant(session, hostBackendConnector, hostStreamingConnector, upvoteDatabase) {

    val queue = mutableStateOf(Queue(mutableStateListOf()))

    private val actualTrackPlayProgress = mutableFloatStateOf(0f)
    private val userDragPlayProgress = mutableFloatStateOf(0f)
    private val isUserDraggingProgress = mutableStateOf(false)

    fun addTrackToQueue(track: Track) {
        if (hasSessionTrack(track.id)) {
            val sessionTrack = getSessionTrack(track.id)
            queue.value.addTrack(sessionTrack)
        } else {
            addOrUpdateSessionTrack(track)
            queue.value.addTrack(getSessionTrack(track.id))
            backendConnector.addTrackToSession(track) {}
        }
    }

    fun removeTrackFromQueue(index: Int) {
        queue.value.removeTrack(index)
    }

    fun moveTrackUpInQueue(index: Int) {
        queue.value.moveTrackUp(index)
    }

    fun moveTrackDownInQueue(index: Int) {
        queue.value.moveTrackDown(index)
    }

    override fun syncState() {
        syncTracksFromBackend()
        refillStreamingQueueIfNeeded()
    }

    fun skip() {
        val playedTrack = queue.value.popFirstTrack()
        (backendConnector as HostBackendConnector).playedTrack(playedTrack)
        voteList.value.removeTrack(playedTrack)
        if (!queue.value.isEmpty()) {
            (streamingConnector as HostStreamingConnector).addTrackToStreamingQueue(
                queue.value.trackAt(0)
            ) {
                (streamingConnector as HostStreamingConnector).skipTrack()
            }
        } else if (!voteList.value.isEmpty()) {
            addTrackToQueue(voteList.value.trackAt(0))
            (streamingConnector as HostStreamingConnector).addTrackToStreamingQueue(
                queue.value.trackAt(0)
            ) {
                (streamingConnector as HostStreamingConnector).skipTrack()
            }
        }
    }

    private fun refillStreamingQueueIfNeeded() {
        if ((streamingConnector as HostStreamingConnector).getPlaybackState().value == PlaybackState.INITIAL) {
            if (!queue.value.isEmpty()) {
                (streamingConnector as HostStreamingConnector).playTrack(
                    queue.value.trackAt(0)
                )
            } else if (!voteList.value.isEmpty()) {
                addTrackToQueue(voteList.value.trackAt(0))
                (streamingConnector as HostStreamingConnector).playTrack(
                    queue.value.trackAt(0)
                )
            }
        } else {
            (streamingConnector as HostStreamingConnector).refillQueueIfNeeded({
                if (!queue.value.isEmpty()) {
                    val playedTrack = queue.value.popFirstTrack()
                    (backendConnector as HostBackendConnector).playedTrack(playedTrack)
                    voteList.value.removeTrack(playedTrack)
                }
                if (!queue.value.isEmpty()) {
                    (streamingConnector as HostStreamingConnector).addTrackToStreamingQueue(
                        queue.value.trackAt(0)
                    )
                } else if (!voteList.value.isEmpty()) {
                    addTrackToQueue(voteList.value.trackAt(0))
                    (streamingConnector as HostStreamingConnector).addTrackToStreamingQueue(
                        queue.value.trackAt(0)
                    )
                } else {
                    (streamingConnector as HostStreamingConnector).setPlaybackState(
                        PlaybackState.INITIAL
                    )
                }
            },
                { progress ->
                    actualTrackPlayProgress.value = progress
                })
        }
    }

    fun pausePlay() {
        (streamingConnector as HostStreamingConnector).pausePlay()
    }

    fun resumePlay() {
        (streamingConnector as HostStreamingConnector).resumePlay()
    }

    fun dragPlayProgress(progress: Float) {
        userDragPlayProgress.value = progress
        isUserDraggingProgress.value = true
    }

    fun finishDragPlayProgress() {
        (streamingConnector as HostStreamingConnector).setPlayProgress(userDragPlayProgress.value){
            actualTrackPlayProgress.value = userDragPlayProgress.value
            isUserDraggingProgress.value = false
        }
    }

    fun getPlayProgress(): Float {
        if (isUserDraggingProgress.value) {
            return userDragPlayProgress.value
        } else {
            return actualTrackPlayProgress.value
        }
    }

    fun getPlaybackState(): MutableState<PlaybackState> {
        return (streamingConnector as HostStreamingConnector).getPlaybackState()
    }

    fun toggleBlockTrack(track: Track) {
        if (hasSessionTrack(track.id)) {
            val sessionTrack = getSessionTrack(track.id)
            sessionTrack.value.setBlocked(!sessionTrack.value.isBlocked())
            (backendConnector as HostBackendConnector).setBlockTrack(
                track,
                blocked = sessionTrack.value.isBlocked()
            )
        } else {
            track.setBlocked(!track.isBlocked())
            backendConnector.addTrackToSession(track) {
                (backendConnector as HostBackendConnector).setBlockTrack(
                    track,
                    blocked = track.isBlocked()
                )
            }
            addOrUpdateSessionTrack(track)
        }
    }

    /*fun removeTrackFromBlockList(track: Track) {
        blockList.value.removeTrack(track)
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.setBlockTrack(track, blocked = false)

    }*/ //TODO probably not needed

    override fun leaveSession() {
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.deleteSession()
    }

    fun isSkipAvailable(): Boolean {
        val isNextTrackInQueue = queue.value.getTracks().size > 1
        val isNextTrackInVoteList = voteList.value.getTracks().size > 1
        if (isNextTrackInQueue || isNextTrackInVoteList) {
            return true
        }
        var isVoteListTrackInQueue = false
        if (!queue.value.isEmpty() && !voteList.value.isEmpty()) {
            isVoteListTrackInQueue = (queue.value.trackAt(0).id == voteList.value.trackAt(0).id)
        }
        return !queue.value.isEmpty() && !isVoteListTrackInQueue
    }

}