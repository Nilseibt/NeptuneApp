package com.example.neptune.data.model.user.test
import android.util.Log
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.track.test.MockTracks
import com.example.neptune.data.model.user.src.User
import org.junit.Assert.assertTrue
import org.junit.Test

class UserTest {


    @Test fun searchTest(){
        val  mockTracks: MockTracks = MockTracks()
        val user = User(Session(12,23,3))
        user.voteList = mockTracks.voteList

        var result = user.search("B")
        //println(result)

        //user.blockList = mockTracks.blockList
        result = user.search("B")
        println(result)

    }
}