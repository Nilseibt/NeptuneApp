package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.Track

/**
 * Class representing a session in Genre Mode, a type of session where tracks are associated with specific genres.
 * @param id The unique identifier of the session.
 * @param timestamp The timestamp of the session creation.
 * @param cooldown The cooldown duration for tracks in the session.
 * @param genres The list of genres allowed in the session.
 */
class GenreSession(
    sessionId: Int,
    timestamp: Int,
    cooldown: Int,
    val genres: List<String>
) : Session(sessionId, timestamp, cooldown, SessionType.GENRE) {

    /**
     * Validates if a track belongs to one of the allowed genres in the session.
     * @param track The track to be validated.
     * @return True if the track is associated with an allowed genre, false otherwise.
     */
    override fun validateTrack(track: Track): Boolean {
        for (genre in track.genres) {
            if (genre in genres) {
                return true
            }
        }
        return false
    }

}