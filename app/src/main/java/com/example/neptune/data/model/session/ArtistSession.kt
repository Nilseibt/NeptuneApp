package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.Track

/**
 * Class representing a session in Artist Mode, a type of session where tracks are associated with specific artists.
 * @param sessionId The 6-digit identifier of the session.
 * @param timestamp The timestamp of the session creation.
 * @param cooldown The cooldown duration for tracks in the session.
 * @param artists The list of artists allowed in the session.
 */
class ArtistSession(
    sessionId: Int,
    timestamp: Int,
    cooldown: Int,
    val artists: List<String>
) : Session(sessionId, timestamp, cooldown, SessionType.ARTIST) {

    /**
     * Validates if a track belongs to one of the allowed artists in the session.
     * @param track The track to be validated.
     * @return True if the track is associated with an allowed artist, false otherwise.
     */
    override fun validateTrack(track: Track): Boolean {
        for (artist in track.artists) {
            if (artist in artists) {
                return true
            }
        }
        return false
    }


}