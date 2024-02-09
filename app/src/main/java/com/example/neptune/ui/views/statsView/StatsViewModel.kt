package com.example.neptune.ui.views.statsView

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.user.User
import com.example.neptune.ui.views.ViewsCollection

/**
 * ViewModel class for controlling the logic of the StatsView.
 *
 * @property user The user of the current session.
 */
class StatsViewModel(
    val user: User
) : ViewModel() {

    /** Mutable state for the most upvoted track. */
    val mostUpvotedTrack = mutableStateOf("")

    /** Mutable state for the most upvoted genre. */
    val mostUpvotedGenre = mutableStateOf("")

    /** Mutable state for the most upvoted artist. */
    val mostUpvotedArtist = mutableStateOf("")

    /** Mutable state for the total played tracks. */
    val totalPlayedTracks = mutableStateOf("")

    /** Mutable state for the session duration. */
    val sessionDuration = mutableStateOf("")

    /** Mutable state for the total participants. */
    val totalParticipants = mutableStateOf("")

    /** Mutable state for the total upvotes. */
    val totalUpvotes = mutableStateOf("")


    /**
     * Initializes the statistics by requesting data from the user.
     */
    init {
        user.requestStatistics { mostUpvotedTrack, mostUpvotedGenre, mostUpvotedArtist,
                                 totalPlayedTracks, sessionDuration, totalParticipants,
                                 totalUpvotes ->
            this.mostUpvotedTrack.value = mostUpvotedTrack
            this.mostUpvotedGenre.value = mostUpvotedGenre
            this.mostUpvotedArtist.value = mostUpvotedArtist
            this.totalPlayedTracks.value = totalPlayedTracks.toString()
            this.sessionDuration.value = sessionDuration
            this.totalParticipants.value = totalParticipants.toString()
            this.totalUpvotes.value = totalUpvotes.toString()
        }
    }

    /**
     * Checks if it's a genre session.
     *
     * @return True if it's a genre session, false otherwise.
     */
    fun isGenreSession(): Boolean {
        return user.session.sessionType == SessionType.GENRE
    }

    /**
     * Handles the back action.
     *
     * @param navController The NavController instance.
     */
    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}