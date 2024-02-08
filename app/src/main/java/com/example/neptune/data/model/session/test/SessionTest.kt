import com.example.neptune.data.model.session.ArtistSession
import com.example.neptune.data.model.session.GenreSession
import com.example.neptune.data.model.session.PlaylistSession
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.track.PlayList
import com.example.neptune.data.model.track.test.MockTracks
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test class for various types of sessions.
 */
class SessionTest {
    val mockTracks = MockTracks()

    /**
     * Test for ArtistSession validation.
     */
    @Test
    fun testArtistSession(){

        val artistSession = ArtistSession(232332,2,3, listOf("alligatoah"))
        assertTrue( artistSession.validateTrack(mockTracks.track1))
        assertFalse(artistSession.validateTrack(mockTracks.track4))
    }

    /**
     * Test for GenreSession validation.
     */
    @Test
    fun testGenreSession(){
        val genreSession = GenreSession(232332,2,3, listOf("irgendwas"))
        assertTrue( genreSession.validateTrack(mockTracks.track1))
    }

    /**
     * Test for General Session validation.
     */
    @Test
    fun testGeneralSession(){
        val artistSession = Session(232332,2,3)
        assertTrue( artistSession.validateTrack(mockTracks.track1))
    }

    /**
     * Test for PlaylistSession validation.
     */
    @Test
    fun testPlayListSession(){
        val playListSession = PlaylistSession(343333,3,4, mockTracks.playList)
        assertTrue(playListSession.validateTrack(mockTracks.track1))
    }
}
