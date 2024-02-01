package com.example.neptune.data.model.user.src


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.PlaybackState

import com.example.neptune.data.model.track.src.Queue
import com.example.neptune.data.model.track.src.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Host(
    session: Session,
    hostBackendConnector: HostBackendConnector,
    hostStreamingConnector: HostStreamingConnector,
    upvoteDatabase: UpvoteDatabase
) : FullParticipant(session, hostBackendConnector, hostStreamingConnector, upvoteDatabase) {

    val queue = mutableStateOf(Queue(mutableStateListOf()))

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
        (streamingConnector as HostStreamingConnector).skipTrack()
    }

    private fun refillStreamingQueueIfNeeded() {
        (streamingConnector as HostStreamingConnector).refillQueueIfNeeded {
            val playedTrack = queue.value.popFirstTrack()
            (backendConnector as HostBackendConnector).playedTrack(playedTrack)
            if (!queue.value.isEmpty()) {
                (streamingConnector as HostStreamingConnector).addTrackToStreamingQueue(
                    queue.value.trackAt(0)
                )
            } else if (!voteList.value.isEmpty()) {
                addTrackToQueue(voteList.value.trackAt(0))
                (streamingConnector as HostStreamingConnector).addTrackToStreamingQueue(
                    voteList.value.trackAt(0)
                )
            }
        }
    }

    fun pausePlay() {
        (streamingConnector as HostStreamingConnector).pausePlay()
    }

    fun resumePlay() {
        (streamingConnector as HostStreamingConnector).resumePlay()
    }

    fun setPlayProgress(percentage: Int) {
        val hostStreamingConnector = streamingConnector as HostStreamingConnector
        hostStreamingConnector.setPlayProgress(percentage)
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

}