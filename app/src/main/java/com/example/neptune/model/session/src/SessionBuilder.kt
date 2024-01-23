package com.example.neptune.model.session.src

class SessionBuilder(val deviceId: Int) {
    var sessionType: SessionType = SessionType.GENERAL
    var artists : List<String> = ArrayList<String>()

    var genres: List<String> = ArrayList<String>()
    var playlistLink: String = String()
    var cooldown: Int         = 60

    fun reset(){
        sessionType = SessionType.GENERAL
        artists = ArrayList<String>()
        genres =ArrayList<String>()
        playlistLink = String()
        cooldown = 60
    }

    fun isReady():Boolean{
       when(sessionType){
           SessionType.GENERAL-> return true
           SessionType.ARTIST-> return artists.isNotEmpty()
           SessionType.GENRE-> return genres.isNotEmpty()
           SessionType.PLAYLIST-> return playlistLink.isNotEmpty()
       }
    }
    fun createSession(){
        //Todo call Backendconnector createSession
        //create Session with returned data

    }
}