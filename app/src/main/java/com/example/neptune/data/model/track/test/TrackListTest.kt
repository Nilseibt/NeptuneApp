package com.example.neptune.data.model.track.test

import com.example.neptune.data.model.track.TrackList
import com.example.neptune.data.model.track.VoteList
import org.junit.Assert.assertFalse
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
        assertTrue(voteList.popFirstTrack() == mockTracks.track4)
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
}