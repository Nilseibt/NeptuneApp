package com.example.neptune.data.model.track.test

import com.example.neptune.data.model.track.src.Queue
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.track.src.TrackList
import com.example.neptune.data.model.track.src.VoteList
import java.sql.Timestamp

/**
 * contains a different tracks their properties and TrackLists for all Tests involving tracks
 */

 class MockTracks{
    val trackId1 = 1
    val name1 = "Baum"
    val artists1 = listOf("alligatoah","kiz")
    val genres1 = listOf("irgendwas", "mit Rap")
    val timeStamp1 = Timestamp(123542453434)
    val imageUrl1 = "https:randomadress"
    val upvote1= 1
    var track1 = Track(trackId1,name1, artists1,genres1,imageUrl1,
        timeStamp1,upvote1,false, false)

    var track2 = Track(1234,"Bier",artists1,genres1,imageUrl1,timeStamp1,
        5,false, false)
    var track3 = Track(2567,"Bär", artists1,genres1,imageUrl1,
        timeStamp1,9,false, false)
    var track4 = Track(5675,"hello",artists1,genres1,imageUrl1,timeStamp1,
        2,false, false)
    var trackList = TrackList(mutableListOf(track1,track2, track3,track4))
    val voteList = VoteList(mutableListOf(track1,track2,track3,track4))
    val blockList = TrackList(mutableListOf(track1))
    val cooldownList = TrackList(mutableListOf(track2))
    val queue = Queue(mutableListOf(track1,track2,track3,track4))

}
