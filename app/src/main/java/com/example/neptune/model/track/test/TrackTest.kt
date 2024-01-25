package com.example.neptune.model.track.test
import com.example.neptune.model.track.src.Track

import org.junit.Assert.assertTrue
import org.junit.Test
import java.sql.Timestamp


class TrackTest {
    val trackId1 = 1
    val name1 = "Baum"
    val artists1 = listOf("alligatoah","kiz")
    val genres1 = listOf("irgendwas", "mit Rap")
    val timeStamp1 = Timestamp(123542453434)
    val imageUrl1 = "https:randomadress"
    val upvote1= 1
    var track1 = Track(trackId1,name1, artists1,genres1,imageUrl1,
        timeStamp1,upvote1,false, false)
    @Test fun addUpvote() {
        track1.addUpvote()
        assertTrue(track1.upvotes == 2)
    }
    @Test fun removeUpvote() {
        track1.removeUpvote()
        track1.removeUpvote()
        track1.removeUpvote()

        assertTrue(track1.upvotes == 0)
    }


}

