package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.Track

/**
 * Base class representing a session, which can be of different types.
 * @param sessionId The unique identifier of the session.
 * @param timestamp The timestamp of the session creation.
 * @param cooldown The cooldown duration for tracks in the session.
 * @param sessionType The type of the session, defaults to [SessionType.GENERAL].
 */
open class Session(
    val sessionId: Int,
    val timestamp: Int,
    val cooldown: Int,
    val sessionType: SessionType = SessionType.GENERAL
) {

    /** Indicates whether any track has been added to the session at all. Handles host deleting the session. */
    var hasAddedTrack = false

    /** Indicates whether the session is closed. Handles host deleting the session. */
    var isSessionClosed = false

    /**
     * default implementation
     * no restrictions in the GENERAL-Mode
     */
    open fun validateTrack(track: Track): Boolean{
        return true
    }

}