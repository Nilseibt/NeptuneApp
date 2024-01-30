package com.example.neptune.data.model.streamingConnector

import com.example.neptune.data.model.track.src.PlayList
import com.example.neptune.data.model.track.src.Track

interface HostStreamingConnector : StreamingConnector {

    fun addTrackToQueue(track: Track)

    fun isQueueEmpty(): Boolean

    fun stopPlay()

    /**
     * skips the currently playing track
     */
    fun skipTrack()

    fun resumePlay()

    fun setPlayProgress(percentage: Int)

    fun isPlaylistLinkValid(): Boolean

    fun getPlaylist(link: String): PlayList

}