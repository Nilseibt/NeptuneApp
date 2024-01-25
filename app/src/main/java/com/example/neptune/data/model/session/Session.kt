package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.src.Track
import java.sql.Timestamp

open class Session(val id:Int,
    val timestamp: Timestamp,
    val cooldown: Int,
    val sessionType: SessionType = SessionType.GENERAL
) {

    /**
     * default implementation
     * no restrictions in the GENERAL-Mode
     */
    open fun validateTrack(track: Track): Boolean{
        return true
    }

}