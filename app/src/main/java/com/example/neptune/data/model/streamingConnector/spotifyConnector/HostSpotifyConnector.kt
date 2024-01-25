package com.example.neptune.data.model.streamingConnector.spotifyConnector

import com.android.volley.RequestQueue
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.model.track.src.PlayList
import com.example.neptune.model.track.src.Track


class HostSpotifyConnector(
    private val volleyQueue: RequestQueue,
    private val authToken: String
) : SpotifyConnector(volleyQueue, authToken), HostStreamingConnector {

    override fun addTrackToQueue(track: Track) {
        //TODO
    }

    override fun isQueueEmpty(): Boolean {
        //TODO
        return true
    }

    override fun stopPlay() {
        //TODO
    }

    override fun resumePlay(){
        //TODO
    }

    override fun setPlayProgress(percentage: Int){
        //TODO
    }

    override fun isPlaylistLinkValid(): Boolean {
        //TODO
        return true
    }

    override fun getPlaylist(link: String): PlayList {
        //TODO
        return PlayList(mutableListOf())
    }


}

