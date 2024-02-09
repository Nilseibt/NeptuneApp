package com.example.neptune

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

import com.android.volley.toolbox.Volley
import com.example.neptune.data.model.backendConnector.HostBackendConnector
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.track.Track
import com.example.neptune.data.model.track.test.MockTracks


import org.junit.runner.RunWith


import org.junit.Test


import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class BackendConnectorTest {
    val mockTracks = MockTracks()


    /**
     * tests the BackendConnector
     * only tests the basic functionality of the backendconnector,
     * the correctness of the backend responses is tested seperatly.
     */
    @Test
    fun testHostBackendConnector() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val hostBackendConnector =
            HostBackendConnector("wersdf", Volley.newRequestQueue(appContext))
        hostBackendConnector.createNewSession(
            "General", 234, artists = listOf("dsf", "dsd"),
            genres = ArrayList<String>()
        ) { sessionId, _ ->
            assertTrue(sessionId in 0..999999)
        }
        hostBackendConnector.addTrackToSession(mockTracks.track1)
        hostBackendConnector.addUpvoteToTrack(mockTracks.track1)
        var list: List<Track>
        hostBackendConnector.getAllTrackData { listOfTracks ->
            list = listOfTracks
        }
        hostBackendConnector.playedTrack(mockTracks.track1)
        hostBackendConnector.addTrackToSession(mockTracks.track2)
        hostBackendConnector.setBlockTrack(mockTracks.track2, true)
        hostBackendConnector.requestStatistics { mostUpvotedTrack, _, _, totalPlayedTracks, _, _, totalUpvotes ->
            assertTrue(totalUpvotes == 1)
            assertTrue(mostUpvotedTrack == mockTracks.track1.name)
            assertTrue(totalPlayedTracks == 1)
        }

        hostBackendConnector.deleteSession()
    }

    /**
     * tests if the particantBackendConnector can join a session created from the
     * HostBackendConnector, and some methods in tandem.
     */
    @Test
    fun testBothBackendConnectors() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hostBackendConnector = HostBackendConnector(
            "wer",
            Volley.newRequestQueue(appContext)
        )
        val participantBackendConnector = ParticipantBackendConnector(
            "fgd",
            Volley.newRequestQueue(appContext)
        )
        val artistList = listOf("dsf", "dsd")
        hostBackendConnector.createNewSession(
            "Artist", 234, artists = artistList,
            genres = ArrayList<String>()
        ) { sessionId, _ ->
            participantBackendConnector.participantJoinSession(sessionId) { success, timestamp, mode, artists, genres
                -> assertTrue(success)
            }
        }
        participantBackendConnector.addTrackToSession(mockTracks.track2)
        participantBackendConnector.addTrackToSession(mockTracks.track3)
        participantBackendConnector.addTrackToSession(mockTracks.track4)
        participantBackendConnector.addTrackToSession(mockTracks.track1)
        hostBackendConnector.setBlockTrack(mockTracks.track2, true)
        hostBackendConnector.addUpvoteToTrack(mockTracks.track4)
        participantBackendConnector.requestStatistics { mostUpvotedTrack, _, _, totalPlayedTracks, _, _, totalUpvotes ->
            assertTrue(totalUpvotes == 1)
            assertTrue(mostUpvotedTrack == mockTracks.track1.name)
            assertTrue(totalPlayedTracks == 1)
        }
        participantBackendConnector.getUserSessionState { userSessionState, sessionId, timestamp, mode, artists, genres ->
            for(i in artists.indices){
                assertTrue(artists[i] == artistList[i])
            }
        }
        participantBackendConnector.participantLeaveSession {}
        hostBackendConnector.deleteSession()

    }
}

