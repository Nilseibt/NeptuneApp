package com.example.neptune.model.session.src

import com.example.neptune.model.track.src.PlayList
import com.example.neptune.model.track.src.Track
import java.sql.Timestamp

class PlaylistSession(id: Int, timestamp: Timestamp, cooldown: Int, sessionType: SessionType,
                   val playList: PlayList) :
    Session(id,timestamp,cooldown, SessionType.PLAYLIST){
    override fun validateTrack(track: Track):Boolean{
        return playList.containsTrack(track)
    }


}