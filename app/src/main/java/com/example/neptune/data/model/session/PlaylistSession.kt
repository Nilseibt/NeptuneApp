package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.PlayList
import com.example.neptune.data.model.track.Track

/**
 * Class representing a session in Playlist Mode, a type of session where tracks are added from a predefined playlist.
 * @param id The unique identifier of the session.
 * @param timestamp The timestamp of the session creation.
 * @param cooldown The cooldown duration for tracks in the session.
 */
class PlaylistSession(
    id: Int,
    timestamp: Int,
    cooldown: Int
) : Session(id, timestamp, cooldown, SessionType.PLAYLIST) {

}