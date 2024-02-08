package com.example.neptune.data.model.track.test

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test class for Queue functionality.
 */
class QueueTest {

    // Using mock data for testing.
    val mockTracks: MockTracks = MockTracks()

    // Retrieving a queue and tracks for testing.
    val queue = mockTracks.queue
    val track4 = mockTracks.track4
    val track3 = mockTracks.track3
    val track1 = mockTracks.track1

    /**
     * Test 'moveTracksInQueue' method.
     */
    @Test
    fun moveTracksInQueue() {
        // Moving track up in the queue and asserting the result.
        queue.moveTrackUp(2)
        assertTrue(queue.popFirstTrack() == track1)
        assertTrue(queue.popFirstTrack() == track3)
    }
}