package com.example.neptune.data.model.user


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.PlaybackState

import com.example.neptune.data.model.track.Queue
import com.example.neptune.data.model.track.Track


/**
 * Represents a host user within a session, extending the functionality of a full participant by
 * providing host-specific actions and state management as well as streaming playback syncing.
 * @param session The session associated with the host user.
 * @param hostBackendConnector The backend connector for the host user.
 * @param hostStreamingConnector The streaming connector for the host user.
 * @param upvoteDatabase The upvote database for the host user.
 */
class Host(
    session: Session,
    hostBackendConnector: HostBackendConnector,
    hostStreamingConnector: HostStreamingConnector,
    upvoteDatabase: UpvoteDatabase
) : FullParticipant(session, hostBackendConnector, hostStreamingConnector, upvoteDatabase) {

    /**
     * The queue of tracks managed by the host.
     */
    val queue = mutableStateOf(Queue(mutableStateListOf()))

    private val actualTrackPlayProgress = mutableFloatStateOf(0f)
    private val userDragPlayProgress = mutableFloatStateOf(0f)
    private val isUserDraggingProgress = mutableStateOf(false)

    private var streamingHint = mutableStateOf("")

    /**
     * Adds a track to the queue.
     * @param track The track to be added to the queue.
     */
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

    /**
     * Removes a track from the queue by its index.
     * @param index The index of the track to be removed.
     */
    fun removeTrackFromQueue(index: Int) {
        queue.value.removeTrack(index)
    }

    /**
     * Moves a track up in the queue by its index.
     * @param index The index of the track to be moved.
     */
    fun moveTrackUpInQueue(index: Int) {
        queue.value.moveTrackUp(index)
    }

    /**
     * Moves a track down in the queue by its index.
     * @param index The index of the track to be moved.
     */
    fun moveTrackDownInQueue(index: Int) {
        queue.value.moveTrackDown(index)
    }

    /**
     * Skips the currently playing track if possible.
     */
    fun skip() {
        (streamingConnector as HostStreamingConnector).checkIfPlayerDeviceAvailable(
            {
                val playedTrack = queue.value.popFirstTrack()
                (backendConnector as HostBackendConnector).playedTrack(playedTrack)
                voteList.value.removeTrack(playedTrack)
                if (!queue.value.isEmpty()) {
                    (streamingConnector as HostStreamingConnector).addTrackToQueue(
                        queue.value.trackAt(0)
                    ) {
                        (streamingConnector as HostStreamingConnector).skip()
                    }
                } else if (!voteList.value.isEmpty()) {
                    addTrackToQueue(voteList.value.trackAt(0))
                    (streamingConnector as HostStreamingConnector).addTrackToQueue(
                        queue.value.trackAt(0)
                    ) {
                        (streamingConnector as HostStreamingConnector).skip()
                    }
                }
            },
            {
                streamingHint.value = NeptuneApp.context.getString(R.string.press_play_in_app)
            }
        )
    }

    /**
     * Overrides the syncState method to synchronize the host's state by also syncing streaming.
     */
    override fun syncState() {
        syncTracksFromBackend()
        getPlayerStateAndRefillQueueIfNeeded()
    }

    private fun getPlayerStateAndRefillQueueIfNeeded() {
        if ((streamingConnector as HostStreamingConnector).getPlaybackState().value == PlaybackState.INITIAL) {
            tryToFillEmptyQueue()
        } else {
            tryToRefillQueue()
        }
    }

    private fun tryToFillEmptyQueue(){
        if (!queue.value.isEmpty()) {
            (streamingConnector as HostStreamingConnector).checkIfPlayerDeviceAvailable(
                {
                    (streamingConnector as HostStreamingConnector).playTrack(
                        queue.value.trackAt(0)
                    )
                },
                {
                    streamingHint.value = NeptuneApp.context.getString(R.string.play_any_track_in_app)
                }
            )
        } else if (!voteList.value.isEmpty()) {
            addTrackToQueue(voteList.value.trackAt(0))
            (streamingConnector as HostStreamingConnector).checkIfPlayerDeviceAvailable(
                {
                    (streamingConnector as HostStreamingConnector).playTrack(
                        queue.value.trackAt(0)
                    )
                },
                {
                    streamingHint.value = NeptuneApp.context.getString(R.string.play_any_track_in_app)
                }
            )
        }
    }

    private fun tryToRefillQueue(){
        (streamingConnector as HostStreamingConnector).checkIfPlayerDeviceAvailable(
            {
                streamingHint.value = ""
                (streamingConnector as HostStreamingConnector).getPlayerStateAndRefillQueueIfNeeded({
                    if (!queue.value.isEmpty()) {
                        val playedTrack = queue.value.popFirstTrack()
                        (backendConnector as HostBackendConnector).playedTrack(playedTrack)
                        voteList.value.removeTrack(playedTrack)
                    }
                    if (!queue.value.isEmpty()) {
                        (streamingConnector as HostStreamingConnector).addTrackToQueue(
                            queue.value.trackAt(0)
                        )
                    } else if (!voteList.value.isEmpty()) {
                        addTrackToQueue(voteList.value.trackAt(0))
                        (streamingConnector as HostStreamingConnector).addTrackToQueue(
                            queue.value.trackAt(0)
                        )
                    } else {
                        (streamingConnector as HostStreamingConnector).setPlaybackState(
                            PlaybackState.INITIAL
                        )
                    }
                }
                ) { progress ->
                    actualTrackPlayProgress.value = progress

                }
            },
            {
                (streamingConnector as HostStreamingConnector).setPlaybackState(
                    PlaybackState.PAUSED
                )
            }
        )
    }

    /**
     * Pauses playback.
     */
    fun pausePlay() {
        (streamingConnector as HostStreamingConnector).pausePlay()
    }

    /**
     * Resumes playback.
     */
    fun resumePlay() {
        (streamingConnector as HostStreamingConnector).checkIfPlayerDeviceAvailable(
            {
                (streamingConnector as HostStreamingConnector).resumePlay()
            },
            {
                streamingHint.value = NeptuneApp.context.getString(R.string.press_play_in_app)
            }
        )
    }

    /**
     * Sets the user's drag play progress.
     * @param progress The progress value to set.
     */
    fun dragPlayProgress(progress: Float) {
        userDragPlayProgress.value = progress
        isUserDraggingProgress.value = true
    }

    /**
     * Finishes dragging the play progress and sets the players progress to the dragged value.
     */
    fun finishDragPlayProgress() {
        (streamingConnector as HostStreamingConnector).checkIfPlayerDeviceAvailable(
            {
                (streamingConnector as HostStreamingConnector).setPlayProgress(userDragPlayProgress.value) {
                    actualTrackPlayProgress.value = userDragPlayProgress.value
                    isUserDraggingProgress.value = false
                }
            },
            {
                isUserDraggingProgress.value = false
                streamingHint.value = NeptuneApp.context.getString(R.string.press_play_in_app)
            }
        )
    }

    /**
     * Retrieves the play progress of the currently playing track.
     * @return The play progress value between 0 and 1.
     */
    fun getPlayProgress(): Float {
        if (isUserDraggingProgress.value) {
            return userDragPlayProgress.value
        } else {
            return actualTrackPlayProgress.value
        }
    }

    /**
     * Retrieves the playback state.
     * @return The playback state.
     */
    fun getPlaybackState(): MutableState<PlaybackState> {
        return (streamingConnector as HostStreamingConnector).getPlaybackState()
    }

    /**
     * Toggles the block status of a track.
     * @param track The track to toggle the block status.
     */
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

    /**
     * Checks if skipping to the next track is available.
     * @return True if skipping is available, false otherwise.
     */
    fun isSkipAvailable(): Boolean {
        val isNextTrackInQueue = queue.value.getTracks().size > 1
        val isNextTrackInVoteList = voteList.value.getTracks().size > 1
        if (isNextTrackInQueue || isNextTrackInVoteList) {
            return true
        }
        var isVoteListTrackInQueue = false
        if (!queue.value.isEmpty() && !voteList.value.isEmpty()) {
            isVoteListTrackInQueue = (queue.value.trackAt(0).id == voteList.value.trackAt(0).id)
        }
        return !queue.value.isEmpty() && !isVoteListTrackInQueue
    }

    /**
     * Retrieves the streaming hint.
     * @return The streaming hint.
     */
    fun getStreamingHint(): MutableState<String> {
        return streamingHint
    }

}