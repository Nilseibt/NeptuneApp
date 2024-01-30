package com.example.neptune.data.model.user.src

import android.health.connect.datatypes.units.Percentage
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.HostSpotifyConnector
import com.example.neptune.data.model.track.src.Queue
import com.example.neptune.data.model.track.src.Track


class Host(
    session: Session,
    hostBackendConnector: HostBackendConnector,
    hostStreamingConnector: HostStreamingConnector
) :
    FullParticipant(session, hostBackendConnector, hostStreamingConnector) {
    val queue = Queue()
    fun addTrackToQueue(index: Int) {
        val track = voteList.trackAt(index)
        queue.addTrack(track)
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
    fun syncState(){

    override fun syncState() {
        super.syncState()
        refillStreamingQueue()
    }

    fun skip() {
        streamingConnector.skipTrack()
        refillStreamingQueue()
    }

    private fun refillStreamingQueue() {
        if (streamingConnector.isQueueEmpty()) {
            if (!queue.isEmpty()) {
                streamingConnector.addTrackToQueue(queue.popFirstTrack())
            } else if (!voteList.isEmpty()) {
                streamingConnector.addTrackToQueue(voteList.popFirstTrack())
            }
        }
    }

    fun stopPlay() {
        streamingConnector.stopPlay()
    }

    fun resumePlay() {
        streamingConnector.resumePlay()
    }

    fun setPlayProgress(percentage: Int) {
        streamingConnector.setPlayProgress(percentage)
    }

    fun addTrackToBlockList(track: Track) {
        blockList.addTrack(track)
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.setBlockTrack(track, blocked = true)
    }

    fun removeTrackFromBlockList(track: Track) {
        blockList.removeTrack(track)
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.setBlockTrack(track, blocked = false)

    }

    override fun leaveSession() {
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.deleteSession()
    }

}