package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.PlayList
import com.example.neptune.data.model.track.Track

class PlaylistSession(
    id: Int,
    timestamp: Int,
    cooldown: Int,
    val playList: PlayList
) :
    Session(id, timestamp, cooldown, SessionType.PLAYLIST) {
    override fun validateTrack(track: Track): Boolean {
        return playList.containsTrack(track)
    }


}