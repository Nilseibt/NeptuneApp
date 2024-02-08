package com.example.neptune.data.model.track.test

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.track.PlayList
import com.example.neptune.data.model.track.Queue
import com.example.neptune.data.model.track.Track
import com.example.neptune.data.model.track.TrackList
import com.example.neptune.data.model.track.VoteList

/**
 * contains a different tracks their properties and TrackLists for all Tests involving tracks
 */

class MockTracks {
    val trackId1 = "1"
    val name1 = "Baum"
    val artists1 = listOf("alligatoah", "kiz")
    val genres1 = mutableListOf("irgendwas", "mit Rap")
    val imageUrl1 = "https:randomadress"
    val upvote1 = 1
    var track1 = Track(
        trackId1, name1, artists1, genres1, imageUrl1, mutableIntStateOf(upvote1),
        mutableStateOf(false), mutableStateOf(false), mutableStateOf(false)
    )

    var track2 = Track(
        "2", "Bier", artists1, genres1, imageUrl1,
        mutableIntStateOf(5), mutableStateOf(false), mutableStateOf(false), mutableStateOf(false)
    )
    var track3 = Track(
        "3",
        "Bär",
        artists1,
        genres1,
        imageUrl1,
        mutableIntStateOf(9),
        mutableStateOf(false),
        mutableStateOf(false),
        mutableStateOf(false)
    )
    var track4 = Track(
        "4", "hello", artists1, genres1, imageUrl1,
        mutableIntStateOf(2), mutableStateOf(false), mutableStateOf(false), mutableStateOf(false)
    )
    var trackList = TrackList(
        mutableStateListOf(
            mutableStateOf(track1),
            mutableStateOf(track2),
            mutableStateOf(track3),
            mutableStateOf(track4)
        )
    )
    val voteList = VoteList(
        mutableStateListOf(
            mutableStateOf(track1),
            mutableStateOf(track2),
            mutableStateOf(track3),
            mutableStateOf(track4)
        )
    )
    val blockList = TrackList(mutableStateListOf(mutableStateOf(track1)))
    val cooldownList = TrackList(mutableStateListOf(mutableStateOf(track2)))
    val queue = Queue(
        mutableStateListOf(
            mutableStateOf(track1),
            mutableStateOf(track2),
            mutableStateOf(track3),
            mutableStateOf(track4)
        )
    )
    val playList = PlayList(
        mutableStateListOf(
            mutableStateOf(track1),
            mutableStateOf(track2),
            mutableStateOf(track3),
            mutableStateOf(track4)
        )
    )

}
