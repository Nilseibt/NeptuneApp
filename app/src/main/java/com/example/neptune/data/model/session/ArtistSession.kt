package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.Track

/**
 * Class for an Session in ArtistMode
 */
class ArtistSession(
    id: Int,
    timestamp: Int,
    cooldown: Int,
    val artists: List<String>
) :
    Session(id, timestamp, cooldown, SessionType.ARTIST) {

    /**
     * validates if a track made from an allowed artists
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