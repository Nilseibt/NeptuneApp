package com.example.neptune.ui.views.statsView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory

/**
 * The composable for the statsView.
 *
 * @param navController The NavController needed to navigate to another view.
 */
@Composable
fun StatsView(navController: NavController) {

    val statsViewModel = viewModel<StatsViewModel>(
        factory = viewModelFactory {
            StatsViewModel(
                NeptuneApp.model.appState.user!!
            )
        }
    )

    BackHandler {
        statsViewModel.onBack(navController)
    }

    NeptuneTheme {

        StatsViewContent(statsViewModel, navController)

    }
}

@Composable
private fun StatsViewContent(statsViewModel: StatsViewModel, navController: NavController) {

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {

        TopBar(onBack = { statsViewModel.onBack(navController) })

        StatsViewBody(statsViewModel)

    }

}

@Composable
private fun StatsViewBody(statsViewModel: StatsViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Box(modifier = Modifier.weight(1f)) {
            StatisticsText()
        }

        Box(modifier = Modifier.weight(1f)) {
            MostPopularTrackText(statsViewModel)
        }

        if (statsViewModel.isGenreSession()) {
            Box(modifier = Modifier.weight(1f)) {
                MostPopularGenreText(statsViewModel)
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            MostPopularArtistText(statsViewModel)
        }

        Box(modifier = Modifier.weight(1f)) {
            TotalNumberTracksText(statsViewModel)
        }

        Box(modifier = Modifier.weight(1f)) {
            TotalNumberUpvotesText(statsViewModel)
        }

        Box(modifier = Modifier.weight(1f)) {
            TotalNumberParticipantsText(statsViewModel)
        }

        Box(modifier = Modifier.weight(1f)) {
            SessionDurationText(statsViewModel)
        }

    }

}

@Composable
private fun StatisticsText() {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.statistics_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }

}

@Composable
private fun MostPopularTrackText(statsViewModel: StatsViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.most_popular_track_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = statsViewModel.mostUpvotedTrack.value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

    }

}

@Composable
private fun MostPopularGenreText(statsViewModel: StatsViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.most_popular_genre_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = statsViewModel.mostUpvotedGenre.value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

    }

}

@Composable
private fun MostPopularArtistText(statsViewModel: StatsViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.most_popular_artist_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = statsViewModel.mostUpvotedArtist.value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }

}

@Composable
private fun TotalNumberTracksText(statsViewModel: StatsViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.total_played_tracks_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = statsViewModel.totalPlayedTracks.value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

    }

}

@Composable
private fun TotalNumberUpvotesText(statsViewModel: StatsViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.total_upvote_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = statsViewModel.totalUpvotes.value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

    }

}

@Composable
private fun TotalNumberParticipantsText(statsViewModel: StatsViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.total_participant_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = statsViewModel.totalParticipants.value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

    }

}

@Composable
private fun SessionDurationText(statsViewModel: StatsViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.session_duration_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = statsViewModel.sessionDuration.value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

    }

}
