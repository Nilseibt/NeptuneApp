package com.example.neptune.data.model.track.test

import com.example.neptune.data.model.track.Track
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test class for Track functionality.
 */
class TrackTest {

    // Using mock data for testing.
    val mockTracks: MockTracks = MockTracks()

    // Retrieving a track for testing.
    val track1: Track = mockTracks.track1

    /**
     * Test 'toggleUpvote' method.
     */
    @Test
    fun toggleUpvote() {
        track1.toggleUpvote()
        assertTrue(track1.getUpvotes() == 2)
        track1.toggleUpvote()
        assertTrue(track1.getUpvotes() == 1)
    }
    @Test
    fun checkAttributes(){
        val track = mockTracks.track1
        track.setBlocked(false)
        assertFalse(track.isBlocked())
        assertFalse(track.hasCooldown())
    }

    /**
     * Test 'logTrack' method.
     */
    @Test
    fun logTrack() {
        System.out.println(track1.toString())
    }

    /**
     * Test 'getArtistNames' method.
     */
    @Test
    fun getArtistNames() {
        assertTrue(track1.getArtistNames() == "alligatoah, kiz")
    }
}
