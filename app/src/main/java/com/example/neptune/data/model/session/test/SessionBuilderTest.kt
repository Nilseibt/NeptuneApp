
package com.example.neptune.data.model.session.test

import com.example.neptune.data.model.session.SessionBuilder
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.track.test.MockTracks

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test class for [SessionBuilder] functions.
 */
class SessionBuilderTest {
    val mockTracks = MockTracks()

    /**
     * Test setting session type from backend string.
     */
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

    /**
     * Test searching matching genres.
     */
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

    /**
     * Test creation of genre session.
     */
    @Test
    fun createGenreSession(){
        val sessionBuilder = SessionBuilder()
        sessionBuilder.setSessionType(SessionType.GENRE)
        sessionBuilder.addEntity("Pop")
        sessionBuilder.setTrackCooldown(10)
        assertTrue(SessionType.GENRE == sessionBuilder.getSessionType())
        assertTrue("Genre" == sessionBuilder.getSessionTypeAsBackendString())
        val session  = sessionBuilder.createSession(212343,345)
        assertTrue(session.sessionId == 212343)
    }

    /**
     * Test creation of general session.
     */
    @Test
    fun createSession(){
        val sessionBuilder = SessionBuilder()
        sessionBuilder.setSessionType(SessionType.GENERAL)
        sessionBuilder.setTrackCooldown(10)
        val session  = sessionBuilder.createSession(212343,345)
        assertTrue(session.sessionId == 212343)
    }

    /**
     * Test creation of artist session.
     */
    @Test
    fun createArtistSession(){
        val sessionBuilder = SessionBuilder()
        sessionBuilder.setSessionType(SessionType.ARTIST)
        sessionBuilder.addEntity("Alligatoah")
        sessionBuilder.setSelectedEntities(listOf("Alligatoah","KIZ"))
        assertTrue("KIZ" in sessionBuilder.getSelectedEntities() )
        sessionBuilder.setTrackCooldown(10)
        val session  = sessionBuilder.createSession(212343,345)
        assertTrue(session.sessionId == 212343)
    }

    /**
     * Test creation of playlist session.
     */
    @Test
    fun createPlaylistSession(){
        val sessionBuilder = SessionBuilder()
        sessionBuilder.setSessionType(SessionType.PLAYLIST)
        sessionBuilder.setPlaylistTracks(mutableListOf(mockTracks.track1,mockTracks.track2))
        sessionBuilder.setTrackCooldown(10)
        val session  = sessionBuilder.createSession(212343,345)
        assertTrue(session.sessionId == 212343)
        assertTrue(session.cooldown == 10)
    }
}
