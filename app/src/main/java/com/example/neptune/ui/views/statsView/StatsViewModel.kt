package com.example.neptune.ui.views.statsView

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.user.User
import com.example.neptune.ui.views.ViewsCollection

class StatsViewModel(
    val user: User
) : ViewModel() {

    val mostUpvotedTrack = mutableStateOf("")
    val mostUpvotedGenre = mutableStateOf("")
    val mostUpvotedArtist = mutableStateOf("")
    val totalPlayedTracks = mutableStateOf("")
    val sessionDuration = mutableStateOf("")
    val totalParticipants = mutableStateOf("")
    val totalUpvotes = mutableStateOf("")

    init {
        user.requestStatistics { mostUpvotedTrack, mostUpvotedGenre, mostUpvotedArtist,
                                 totalPlayedTracks, sessionDuration, totalParticipants,
                                 totalUpvotes  ->
            this.mostUpvotedTrack.value = mostUpvotedTrack
            this.mostUpvotedGenre.value = mostUpvotedGenre
            this.mostUpvotedArtist.value = mostUpvotedArtist
            this.totalPlayedTracks.value = totalPlayedTracks.toString()
            this.sessionDuration.value = sessionDuration
            this.totalParticipants.value = totalParticipants.toString()
            this.totalUpvotes.value = totalUpvotes.toString()
        }
    }

    fun isGenreSession(): Boolean{
        return user.session.sessionType == SessionType.GENRE
    }

    fun onShareStatisticsImage() {
        //TODO
    }

    fun onOpenInfo(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}