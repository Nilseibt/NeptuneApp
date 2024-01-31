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
    val track2 = mockTracks.track2
    val track3 = mockTracks.track3

    /**
     * Test 'moveTracksInQueue' method.
     */
    @Test
    fun moveTracksInQueue() {
        // Moving track up in the queue and asserting the result.
        queue.moveTrackUp(1)
        assertTrue(queue.popFirstTrack() == track2)

        // Moving track down in the queue and asserting the result.
        queue.moveTrackDown(0)
        assertTrue(queue.popFirstTrack() == track3)
    }
}