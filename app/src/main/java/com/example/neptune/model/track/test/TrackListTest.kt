package com.example.neptune.model.track.test

import com.example.neptune.model.track.src.Track
import com.example.neptune.model.track.src.TrackList
import com.example.neptune.model.track.src.VoteList
import org.junit.Assert.assertTrue
import org.junit.Test
import java.sql.Timestamp

class TrackListTest {
    val trackId1 = 1
    val name1 = "Baum"
    val artists1 = listOf("alligatoah","kiz")
    val genres1 = listOf("irgendwas", "mit Rap")
    val timeStamp1 = Timestamp(123542453434)
    val imageUrl1 = "https:randomadress"
    val upvote1= 10
    var track1 = Track(trackId1,name1, artists1,genres1,imageUrl1,
        timeStamp1,upvote1,false)
    var track2 = Track(1234,"hello",artists1,genres1,imageUrl1,timeStamp1,5,false)
    var track3 = Track(2567,name1, artists1,genres1,imageUrl1,
        timeStamp1,9,false)
    var track4 = Track(5675,"hello",artists1,genres1,imageUrl1,timeStamp1,2,false)
    var trackList = TrackList(mutableListOf(track1,track2, track3,track4))
    var voteList = VoteList(mutableListOf(track1,track2,track3,track4))
    @Test fun popFirstTrack(){
        val track = trackList.popFirstTrack()
        assertTrue(track == track1)
    }
    @Test fun sortByUpvote(){
        voteList.sortByUpvote()
        assertTrue(voteList.popFirstTrack() == track4)
        assertTrue(voteList.popFirstTrack() == track2)
    }
}