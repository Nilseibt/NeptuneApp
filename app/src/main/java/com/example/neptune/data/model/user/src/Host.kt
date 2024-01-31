package com.example.neptune.data.model.user.src


import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector

import com.example.neptune.data.model.track.src.Queue
import com.example.neptune.data.model.track.src.Track


class Host(
    session: Session,
    hostBackendConnector: HostBackendConnector,
    hostStreamingConnector: HostStreamingConnector,
    upvoteDatabase: UpvoteDatabase
) :
    FullParticipant(session, hostBackendConnector, hostStreamingConnector, upvoteDatabase) {
    val queue = Queue()
    fun addTrackToQueue(index: Int) {
        val track = voteList.value.trackAt(index)
        queue.addTrack(mutableStateOf( track))
    }

    fun removeTrackFromQueue(index: Int) {
        queue.removeTrack(index)
    }

    fun moveTrackUpInQueue(index: Int) {
        queue.moveTrackUp(index)
    }

    fun removeTrackDownInQueue(index: Int) {
        queue.moveTrackDown(index)
    }

    override fun syncState() {
        syncTracksFromBackend()
        //refillStreamingQueue() //TODO
    }

    fun skip() {
        val hostStreamingConnector = streamingConnector as HostStreamingConnector
        hostStreamingConnector.skipTrack()
        refillStreamingQueue()
    }

    private fun refillStreamingQueue() {
        val hostStreamingConnector = streamingConnector as HostStreamingConnector
        if (hostStreamingConnector.isQueueEmpty()) {
            if (!queue.isEmpty()) {
                hostStreamingConnector.addTrackToQueue(queue.popFirstTrack())
            } else if (!voteList.value.isEmpty()) {
                hostStreamingConnector.addTrackToQueue(voteList.value.popFirstTrack())
            }
        }
    }

    fun stopPlay() {
        val hostStreamingConnector = streamingConnector as HostStreamingConnector
        hostStreamingConnector.stopPlay()
    }

    fun resumePlay() {
        val hostStreamingConnector = streamingConnector as HostStreamingConnector
        hostStreamingConnector.resumePlay()
    }

    fun setPlayProgress(percentage: Int) {
        val hostStreamingConnector = streamingConnector as HostStreamingConnector
        hostStreamingConnector.setPlayProgress(percentage)
    }

    fun addTrackToBlockList(track: Track) {
        blockList.value.addTrack(mutableStateOf( track))
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.setBlockTrack(track, blocked = true)
    }

    fun removeTrackFromBlockList(track: Track) {
        blockList.value.removeTrack(track)
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.setBlockTrack(track, blocked = false)

    }

    override fun leaveSession() {
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.deleteSession()
    }

}