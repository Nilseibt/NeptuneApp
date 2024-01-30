package com.example.neptune.data.model.user.src

import android.health.connect.datatypes.units.Percentage
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.HostSpotifyConnector
import com.example.neptune.data.model.track.src.Queue
import com.example.neptune.data.model.track.src.Track


class Host(session: Session,hostBackendConnector: HostBackendConnector,val hostStreamingConnector: HostStreamingConnector) :
    FullParticipant(session,hostBackendConnector,hostStreamingConnector){
        val queue = Queue()
    fun addTrackToQueue(index:Int){
        val track = voteList.trackAt(index)
        queue.addTrack(track)
    }
    fun removeTrackFromQueue(index:Int){
        queue.removeTrack(index)
    }
    fun moveTrackUpInQueue(index:Int){
        queue.moveTrackUp(index)
    }
    fun removeTrackDownInQueue(index: Int){
        queue.moveTrackDown(index)
    }
    fun syncState(){

    }
    fun skip(){
        hostStreamingConnector.skipTrack()
    }
    private fun refillStreamingQueue(){
        if(!queue.isEmpty()){
            hostStreamingConnector.addTrackToQueue(queue.popFirstTrack())
        }else if (!voteList.isEmpty()){
            hostStreamingConnector.addTrackToQueue(voteList.popFirstTrack())
        }
    }
    fun stopPlay(){
        hostStreamingConnector.stopPlay()
    }
    fun resumePlay(){
        hostStreamingConnector.resumePlay()
    }
    fun setPlayProgress(percentage: Int){
        hostStreamingConnector.setPlayProgress(percentage)
    }
    fun addTrackToBlockList(track: Track){
        blockList.addTrack(track)
        val hostBackendConnector = backendConnector as HostBackendConnector
        hostBackendConnector.setBlockTrack(track, true)
    }

}