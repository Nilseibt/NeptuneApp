package com.example.neptune.data.model.user.src

import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.streamingConnector.HostStreamingConnector
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.streamingConnector.spotifyConnector.SpotifyConnector
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.track.src.TrackList

open class FullParticipant(
    session: Session, backendConnector: BackendConnector,
    val streamingConnector: StreamingConnector,
    upvoteDatabase: UpvoteDatabase
) : User(session, backendConnector, upvoteDatabase) {

    override fun search(input: String) {
        var resultLimit = 0
        if (session.sessionType == SessionType.GENERAL) {
            resultLimit = 20
        } else if (session.sessionType == SessionType.ARTIST) {
            resultLimit = 50
        } else if (session.sessionType == SessionType.GENRE) {
            resultLimit = 30
        }
        searchList.value.clear()
        if (session.sessionType == SessionType.GENERAL || session.sessionType == SessionType.ARTIST) {
            streamingConnector.search(input, resultLimit) { resultList ->
                resultList.forEach { track ->
                    if (session.validateTrack(track)) {
                        if (hasSessionTrack(track.id)) {
                            searchList.value.addTrack(getSessionTrack(track.id))
                        } else {
                            searchList.value.addTrack(mutableStateOf(track))
                        }
                    }
                }
            }
        } else if (session.sessionType == SessionType.GENRE) {
            streamingConnector.searchWithGenres(input, resultLimit) { resultList ->
                resultList.forEach { track ->
                    if (session.validateTrack(track)) {
                        if (hasSessionTrack(track.id)) {
                            searchList.value.addTrack(getSessionTrack(track.id))
                        } else {
                            searchList.value.addTrack(mutableStateOf(track))
                        }
                    }
                }
            }
        }
    }

}