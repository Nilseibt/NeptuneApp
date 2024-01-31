package com.example.neptune.data.model.user.test
import androidx.compose.runtime.mutableStateOf
import com.android.volley.toolbox.Volley
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.backendConnector.ParticipantBackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.track.test.MockTracks
import com.example.neptune.data.model.user.src.User
import org.junit.Test

class UserTest {
    val  mockTracks: MockTracks = MockTracks()
    val backendConnector =ParticipantBackendConnector("234fs", Volley.newRequestQueue(NeptuneApp.context))
    @Test fun searchTest(){

        val user = User(Session(12,23,3),backendConnector)
        user.voteList = mutableStateOf(mockTracks.voteList)

        var result = user.search("B")
        //println(result)

        //user.blockList = mockTracks.blockList
        result = user.search("B")
        println(result)

    }
}