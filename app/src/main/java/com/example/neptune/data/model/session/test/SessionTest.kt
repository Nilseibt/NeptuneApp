package com.example.neptune.data.model.session.test

import com.example.neptune.data.model.session.SessionBuilder
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.track.src.Track
import org.junit.Assert
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.sql.Timestamp

class SessionTest {

    @Test
    fun setSessionTypeFromBackendString(){
        val sessionBuilder = SessionBuilder()
        sessionBuilder.setSessionTypeFromBackendString("General")
        assertTrue(sessionBuilder.getSessionType() == SessionType.GENERAL)
        sessionBuilder.setSessionTypeFromBackendString("Artist")
        assertTrue(sessionBuilder.getSessionType() == SessionType.ARTIST)
        sessionBuilder.setSessionTypeFromBackendString("Genre")
        assertTrue(sessionBuilder.getSessionType() == SessionType.GENRE)
        sessionBuilder.setSessionTypeFromBackendString("Playlist")
        assertTrue(sessionBuilder.getSessionType() == SessionType.PLAYLIST)
    }
    @Test
    fun searchMatchingGenres() {
        val sessionBuilder = SessionBuilder()
        sessionBuilder.setSessionType(SessionType.GENRE)
        val test = sessionBuilder.searchMatchingGenres("pop")
        val pop_genres = arrayOf("Pop", "C-pop", "J-pop", "K-pop", "Bow Pop", "Britpop", "Europop",
            "Hip Pop", "Pop Emo", "Pop Rap", "Popgaze", "Arab Pop", "Cantopop",
            "Etherpop", "Folk-pop", "Mandopop", "Math Pop", "Pop Punk", "Pop Rock", "Soda Pop")
        for( i in test.indices){
            assertTrue(test[i].equals(pop_genres[i]))
        }
    }
    @Test
    fun createSession(){
        val sessionBuilder = SessionBuilder()
        sessionBuilder.setSessionType(SessionType.GENRE)
        sessionBuilder.addEntity("Pop")
        sessionBuilder.setTrackCooldown(10)
        val session  = sessionBuilder.createSession(212343,345)
        assertTrue(session.id == 212343)
    }


}