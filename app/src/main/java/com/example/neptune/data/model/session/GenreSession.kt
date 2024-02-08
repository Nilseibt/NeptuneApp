package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.Track

class GenreSession(
    id: Int,
    timestamp: Int,
    cooldown: Int,
    val genres: List<String>
) :
    Session(id, timestamp, cooldown, SessionType.GENRE) {
    override fun validateTrack(track: Track): Boolean {
        for (genre in track.genres) {
            if (genre in genres) {
                return true
            }
        }
        return false
    }


}