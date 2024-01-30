package com.example.neptune.data.model.user.src

import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.spotifyConnector.HostSpotifyConnector
import com.example.neptune.data.model.track.src.Queue


class Host(session: Session,hostBackendConnector: HostBackendConnector,hostSpotifyConnector: HostSpotifyConnector) :
    FullParticipant(session,hostBackendConnector,hostSpotifyConnector){
        val queue = Queue()
    fun addTrackToQueue(index:Int){
        var track = voteList.trackAt(index)
        queue.addTrack(track)
    }
    fun removeTrackFromQueue(index:Int){
        queue.removeTrack(index)
    }


}