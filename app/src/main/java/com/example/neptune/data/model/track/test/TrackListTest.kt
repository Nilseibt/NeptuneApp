package com.example.neptune.data.model.track.test

import com.example.neptune.data.model.track.src.TrackList
import com.example.neptune.data.model.track.src.VoteList
import org.junit.Assert.assertTrue
import org.junit.Test


class TrackListTest {
    val mockTracks :MockTracks = MockTracks()
    val trackList : TrackList = mockTracks.trackList
    val voteList : VoteList = mockTracks.voteList
    @Test fun popFirstTrack(){
        val track = trackList.popFirstTrack()
        assertTrue(track == mockTracks.track1)
    }
    @Test fun sortByUpvote(){
        voteList.sortByUpvote()
        assertTrue(voteList.popFirstTrack() == mockTracks.track4)
        assertTrue(voteList.popFirstTrack() == mockTracks.track2)
    }
}