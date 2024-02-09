package com.example.neptune.data.model.streamingConnector

import androidx.compose.runtime.MutableState
import com.example.neptune.data.model.streamingConnector.spotifyConnector.PlaybackState
import com.example.neptune.data.model.track.Track

/**
 * Interface defining additional methods specific to a host streaming connector.
 * Extends [StreamingConnector].
 */
interface HostStreamingConnector : StreamingConnector {

    /**
     * Plays the specified track.
     * @param track The track to play.
     * @param positionMs The position in milliseconds to start playback from.
     */
    fun playTrack(track: Track, positionMs: Int = 0)

    /**
     * Adds the specified track to the streaming queue.
     * @param track The track to add to the queue.
     * @param onCallback Optional callback function to execute after adding the track.
     */
    fun addTrackToQueue(track: Track, onCallback: () -> Unit = {})

    /**
     * Retrieves the player state and refills the queue if needed.
     * @param onRefillQueue Callback function to execute when the queue needs to be refilled.
     * @param updatePlayProgress Callback function to update the playback progress.
     */
    fun getPlayerStateAndRefillQueueIfNeeded(onRefillQueue: () -> Unit, updatePlayProgress: (Float) -> Unit)

    /**
     * Pauses playback.
     */
    fun pausePlay()

    /**
     * Skips to the next track in the playback queue.
     */
    fun skip()

    /**
     * Resumes playback.
     */
    fun resumePlay()

    /**
     * Sets the playback progress to the specified value.
     * @param progress The playback progress as a float value.
     * @param onCallback Optional callback function to execute after setting the progress.
     */
    fun setPlayProgress(progress: Float, onCallback: () -> Unit)

    /**
     * Retrieves the current playback state.
     * @return The playback state.
     */
    fun getPlaybackState(): MutableState<PlaybackState>

    /**
     * Sets the playback state to the specified value.
     * @param playbackState The playback state to set.
     */
    fun setPlaybackState(playbackState: PlaybackState)

    /**
     * Checks if the player device is available.
     * @param onDeviceAvailable Callback function to execute if a player device is available.
     * @param onNoDeviceAvailable Callback function to execute if no player device is available.
     */
    fun checkIfPlayerDeviceAvailable(onDeviceAvailable: () -> Unit, onNoDeviceAvailable: () -> Unit)

}