package com.example.neptune.model.session.src
import com.example.neptune.model.track.src.Track
import java.sql.Timestamp

class GenreSession( id: Int,timestamp: Timestamp, cooldown: Int, sessionType: SessionType,
                     val genres: List<String>) :
    Session(id,timestamp,cooldown, SessionType.GENRE){
    override fun validateTrack(track: Track):Boolean{
        for (genres in track.genres){
            if(genres in genres){
                return  true
            }
        }
        return false
    }


}