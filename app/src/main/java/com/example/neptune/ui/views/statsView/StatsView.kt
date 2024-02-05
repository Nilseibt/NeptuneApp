package com.example.neptune.ui.views.statsView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun StatsView(navController: NavController) {

    val statsViewModel = viewModel<StatsViewModel>(
        factory = viewModelFactory {
            StatsViewModel(
                NeptuneApp.model.user!!
            )
        }
    )

    Column {

        TopBar(onBack = { statsViewModel.onBack(navController) })

        Text(text = "Track mit meisten Upvotes: " + statsViewModel.mostUpvotedTrack.value)
        if(statsViewModel.isGenreSession()) {
            Text(text = "Genre mit meisten Upvotes: " + statsViewModel.mostUpvotedGenre.value)
        }
        Text(text = "Artist mit meisten Upvotes: " + statsViewModel.mostUpvotedArtist.value)
        Text(text = "Gespielte Tracks: " + statsViewModel.totalPlayedTracks.value)
        Text(text = "Session-Dauer: " + statsViewModel.sessionDuration.value)
        Text(text = "Teilnehmer-Anzahl: " + statsViewModel.totalParticipants.value)
        Text(text = "Upvote-Anzahl: " + statsViewModel.totalUpvotes.value)
    }


    BackHandler {
        statsViewModel.onBack(navController)
    }
}

@Preview
@Composable
fun StatsPreview() {

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {

        TopBar {}

    }

}

/*
Column {

        Button(onClick = { statsViewModel.onOpenInfo(navController) }) {
            Text(text = "Info öffnen (Icon)")
        }

        Text(text = "Session Stats (TopBar Beschriftung)")

        Button(onClick = { statsViewModel.onBack(navController) }) {
            Text(text = "Statistiken öffnen (Icon)")
        }

        Text(text = "Track mit meisten Upvotes: " + statsViewModel.mostUpvotedTrack.value)
        if(statsViewModel.isGenreSession()) {
            Text(text = "Genre mit meisten Upvotes: " + statsViewModel.mostUpvotedGenre.value)
        }
        Text(text = "Artist mit meisten Upvotes: " + statsViewModel.mostUpvotedArtist.value)
        Text(text = "Gespielte Tracks: " + statsViewModel.totalPlayedTracks.value)
        Text(text = "Session-Dauer: " + statsViewModel.sessionDuration.value)
        Text(text = "Teilnehmer-Anzahl: " + statsViewModel.totalParticipants.value)
        Text(text = "Upvote-Anzahl: " + statsViewModel.totalUpvotes.value)
    }
 */
