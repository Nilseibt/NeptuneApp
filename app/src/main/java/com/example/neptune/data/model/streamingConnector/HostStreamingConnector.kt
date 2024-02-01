package com.example.neptune.data.model.streamingConnector

import androidx.compose.runtime.MutableState
import com.example.neptune.data.model.streamingConnector.spotifyConnector.PlaybackState
import com.example.neptune.data.model.track.src.PlayList
import com.example.neptune.data.model.track.src.Track

interface HostStreamingConnector : StreamingConnector {

    fun addTrackToStreamingQueue(track: Track)

    fun refillQueueIfNeeded(onRefillQueue: () -> Unit)

    fun pausePlay()

    /**
     * skips the currently playing track
     */
    fun skipTrack()

    fun resumePlay()

    fun setPlayProgress(percentage: Int)

    fun isPlaylistLinkValid(): Boolean

    fun getPlaylist(link: String): PlayList

    fun getPlaybackState(): MutableState<PlaybackState>

}