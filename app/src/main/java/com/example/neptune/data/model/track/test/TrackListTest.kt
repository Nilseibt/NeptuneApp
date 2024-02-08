package com.example.neptune.data.model.track.test

import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.track.TrackList
import com.example.neptune.data.model.track.VoteList
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test


/**
 * Test suite for TrackList functionality.
 */

class TrackListTest {
    // Using mock data for testing.
    val mockTracks: MockTracks = MockTracks()
    val trackList: TrackList = mockTracks.trackList
    val voteList: VoteList = mockTracks.voteList

    /**
     * Test 'popFirstTrack' method.
     */
    @Test
    fun popFirstTrack() {
        val track = trackList.popFirstTrack()
        assertTrue(track == mockTracks.track1)
    }

    /**
     * Test 'sortByUpvote' method.
     */
    @Test
    fun sortByUpvote() {
        voteList.sortByUpvote()
        assertTrue(voteList.popFirstTrack() == mockTracks.track3)
        assertTrue(voteList.popFirstTrack() == mockTracks.track2)
    }

    /**
     * Test 'containsTrack' method.
     */
    @Test
    fun containsTrack() {
        assertTrue(voteList.containsTrack(mockTracks.track1))
        voteList.popFirstTrack()
        assertFalse(voteList.containsTrack(mockTracks.track1))
    }

    /**
     * tests some basic operations on TrackLists
     */
    @Test
    fun commonOperations(){
        trackList.removeTrack(mockTracks.track1)
        assertFalse(trackList.containsTrack(mockTracks.track1))
        trackList.addTrack(mockTracks.track1)
        trackList.toString()
        assertTrue(mockTracks.track2 == trackList.trackAt(0))
        assertTrue(mockTracks.track2 == trackList.popFirstTrack())
        trackList.exchangeTrack(0, mutableStateOf( mockTracks.track2))
        assertTrue(mockTracks.track2 == trackList.popFirstTrack())
        trackList.addTrack(mutableStateOf(mockTracks.track1))
        trackList.clear()
        trackList.getTracks()
        assertTrue(trackList.isEmpty())
    }
}