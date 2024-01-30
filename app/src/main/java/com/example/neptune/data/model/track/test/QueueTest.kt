package com.example.neptune.data.model.track.test

import android.util.Log
import com.example.neptune.data.model.track.src.Track
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class QueueTest {
    val mockTracks: MockTracks = MockTracks()
    val queue=  mockTracks.queue
    val track1 = mockTracks.track1
    val track2 = mockTracks.track2
    val track3 = mockTracks.track3
    val track4 = mockTracks.track4

    @Test fun moveTrackUp(){
        queue.moveTrackUp(1)
        assertArrayEquals(arrayOf( queue.tracks), arrayOf(track2,track1,track3,track4))
    }
}