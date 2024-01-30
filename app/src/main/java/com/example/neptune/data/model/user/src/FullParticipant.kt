package com.example.neptune.data.model.user.src
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnector
import com.example.neptune.data.model.track.src.TrackList

open class FullParticipant(session: Session, backendConnector: BackendConnector,
                           val streamingConnector: StreamingConnector
):
    User(session,backendConnector) {

    override fun search(input: String): TrackList {
        return super.search(input)
    }
}