package com.example.neptune.data.model.track.test

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.neptune.data.model.track.PlayList
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test
/**
 * Test class for Playlist functionality.
 */
class PlayListTest {

    // Using mock data for testing.
    val mockTracks: MockTracks = MockTracks()


    /**
     * Test 'search' method.
     */
    @Test
    fun search() {
        // Creating a playlist for testing.
        val playList = mockTracks.playList

        // Searching for tracks containing the letter "B".
        val result = PlayList(playList.search("B").toMutableStateList())
        // Asserting that the result contains specific tracks.
        assertTrue( result.containsTrack(mockTracks.track1))
    }
}