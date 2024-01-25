package com.example.neptune.data.model.session

import com.example.neptune.data.model.track.src.Track
import java.sql.Timestamp

class ArtistSession(id: Int, timestamp: Timestamp, cooldown: Int, sessionType: SessionType,
                    val artists: List<String>) :
                     Session(id,timestamp,cooldown, SessionType.ARTIST){
       override fun validateTrack(track: Track):Boolean{
           for (artist in track.artists){
               if(artist in artists){
                   return  true
               }
           }
           return false
       }


}