package com.example.neptune.data.model.streamingConnector

import androidx.compose.runtime.MutableState
import com.example.neptune.data.model.streamingConnector.spotifyConnector.PlaybackState
import com.example.neptune.data.model.track.src.PlayList
import com.example.neptune.data.model.track.src.Track

interface HostStreamingConnector : StreamingConnector {

    fun playTrack(track: Track, positionMs: Int = 0)

    fun addTrackToStreamingQueue(track: Track, onCallback: () -> Unit = {})

    fun refillQueueIfNeeded(onRefillQueue: () -> Unit, updatePlayProgress: (Float) -> Unit)

    fun pausePlay()

    /**
     * skips the currently playing track
     */
    fun skipTrack()

    fun resumePlay()

    fun setPlayProgress(progress: Float, onCallback: () -> Unit)

    fun isPlaylistLinkValid(): Boolean

    fun getPlaylist(link: String): PlayList

    fun getPlaybackState(): MutableState<PlaybackState>

    fun setPlaybackState(playbackState: PlaybackState)

}