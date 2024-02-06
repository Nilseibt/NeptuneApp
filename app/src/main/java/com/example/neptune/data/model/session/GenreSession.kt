package com.example.neptune.data.model.session

import androidx.compose.ui.text.capitalize
import com.example.neptune.data.model.track.src.Track
import java.sql.Timestamp
import java.util.Locale

class GenreSession(
    id: Int,
    timestamp: Int,
    cooldown: Int,
    val genres: List<String>
) :
    Session(id, timestamp, cooldown, SessionType.GENRE) {
    override fun validateTrack(track: Track): Boolean {
        for (genre in track.genres) {
            if (genre.capitalize() in genres) {
                return true
            }
        }
        return false
    }


}