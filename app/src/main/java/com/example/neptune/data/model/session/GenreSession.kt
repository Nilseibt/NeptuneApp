package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.src.Track
import java.sql.Timestamp

class GenreSession(
    id: Int,
    timestamp: Int,
    cooldown: Int,
    val genres: List<String>
) :
    Session(id, timestamp, cooldown, SessionType.GENRE) {
    override fun validateTrack(track: Track): Boolean {
        for (genres in track.genres) {
            if (genres in genres) {
                return true
            }
        }
        return false
    }


}