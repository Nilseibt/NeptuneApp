package com.example.neptune.data.model.user

import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.backendConnector.BackendConnector
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.streamingConnector.StreamingConnector
import com.example.neptune.data.model.track.Track

/**
 * Represents a participant with full access rights in a session, including the ability to search for tracks.
 * @param session The session associated with the participant.
 * @param backendConnector The backend connector for the participant.
 * @param streamingConnector The streaming connector for the participant.
 * @param upvoteDatabase The upvote database for the participant.
 */
open class FullParticipant(
    session: Session, backendConnector: BackendConnector,
    val streamingConnector: StreamingConnector,
    upvoteDatabase: UpvoteDatabase
) : User(session, backendConnector, upvoteDatabase) {

    /**
     * Searches for tracks based on the input query.
     * The result limit is determined based on the session type.
     * Searches with the streaming service except the playlist mode.
     * @param input The search query.
     */
    override fun search(input: String) {
        var resultLimit = 0
        if (session.sessionType == SessionType.GENERAL) {
            resultLimit = 20
        } else if (session.sessionType == SessionType.ARTIST) {
            resultLimit = 50
        } else if (session.sessionType == SessionType.GENRE) {
            resultLimit = 30
        }
        if (session.sessionType == SessionType.GENERAL || session.sessionType == SessionType.ARTIST) {
            streamingConnector.search(input, resultLimit) { resultList ->
                buildSearchListFromResults(resultList)
            }
        } else if (session.sessionType == SessionType.GENRE) {
            streamingConnector.searchWithGenres(input, resultLimit) { resultList ->
                buildSearchListFromResults(resultList)
            }
        }
        else if (session.sessionType == SessionType.PLAYLIST) {
            super.search(input)
        }
    }

    private fun buildSearchListFromResults(resultList: MutableList<Track>){
        searchList.value.clear()
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