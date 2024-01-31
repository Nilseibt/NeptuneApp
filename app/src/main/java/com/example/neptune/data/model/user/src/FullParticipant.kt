package com.example.neptune.data.model.user.src

import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnector
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.track.src.TrackList

open class FullParticipant(
    session: Session, backendConnector: BackendConnector,
    val streamingConnector: StreamingConnector
) : User(session, backendConnector) {

    override fun search(input: String) {
        searchList.value.clear()
        streamingConnector.search(input) { resultList ->
            resultList.forEach { track ->
                if (hasSessionTrack(track.id)) {
                    searchList.value.addTrack(getSessionTrack(track.id))
                } else {
                    searchList.value.addTrack(mutableStateOf( track))
                }
            }
        }
    }
}