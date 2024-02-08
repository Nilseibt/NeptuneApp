package com.example.neptune.ui.views.controlView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.user.Host
import com.example.neptune.ui.commons.SessionInfoBar
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.commons.TrackListComposable
import com.example.neptune.ui.commons.TrackListType
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
import kotlinx.coroutines.delay

/**
 * The composable for the controlView.
 *
 * @param navController the NavController needed to navigate to another view
 */
@Composable
fun ControlView(navController: NavController) {

    val controlViewModel = viewModel<ControlViewModel>(
        factory = viewModelFactory {
            ControlViewModel(
                NeptuneApp.model.user as Host
            )
        }
    )

    BackHandler {
        controlViewModel.onBack(navController)
    }

    LaunchedEffect(
        key1 = Unit,
        block = {
            while (true) {
                controlViewModel.syncState()
                delay(3000)
            }
        }
    )

    NeptuneTheme {

        ControlViewContent(controlViewModel, navController)

    }

}

@Composable
private fun ControlViewContent(controlViewModel: ControlViewModel, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        TopBar(onBack = { controlViewModel.onBack(navController) })

        SessionInfoBar(
            onStatistics = { controlViewModel.onOpenStats(navController) },
            onInfo = { controlViewModel.onOpenInfo(navController) },
            description = controlViewModel.getTopBarDescription()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            //TODO Stephan please make nice
            if (controlViewModel.isStreamingHintAvailable()) {
                Text(text = controlViewModel.getStreamingHint(), color = Color.Red)
            }

            QueueText()

            Box(modifier = Modifier.weight(7f)) {

                TrackListComposable(
                    tracks = controlViewModel.getQueueList(),
                    trackListType = TrackListType.HOST_QUEUE,
                    onToggleUpvote = { controlViewModel.onToggleUpvote(it) },
                    onToggleDropdown = { controlViewModel.onToggleDropdownQueue(it) },
                    isDropdownExpanded = { controlViewModel.isDropdownExpandedQueue(it) },
                    onRemoveFromQueue = { controlViewModel.onRemoveFromQueue(it) },
                    onToggleBlock = { controlViewModel.onToggleBlock(it) },
                    onMoveUp = { controlViewModel.onMoveUp(it) },
                    onMoveDown = { controlViewModel.onMoveDown(it) }
                )

            }

            VoteListText()

            Box(modifier = Modifier.weight(7f)) {

                TrackListComposable(
                    tracks = controlViewModel.getVoteList(),
                    trackListType = TrackListType.HOST_VOTE,
                    onToggleUpvote = { controlViewModel.onToggleUpvote(it) },
                    onToggleDropdown = { controlViewModel.onToggleDropdownVote(it) },
                    isDropdownExpanded = { controlViewModel.isDropdownExpandedVote(it) },
                    onAddToQueue = { controlViewModel.onAddToQueue(it) },
                    onToggleBlock = { controlViewModel.onToggleBlock(it) }
                )

            }

            Box(modifier = Modifier.weight(1f)) {
                TrackControlBar(controlViewModel)
            }

            Box(modifier = Modifier.weight(2f)) {
                SearchButton(controlViewModel, navController)
            }

        }

    }

    if (controlViewModel.isDeleteSessionDialogShown()) {
        TerminateSessionDialog(controlViewModel, navController)
    }

}

@Composable
private fun QueueText() {

    Text(
        text = stringResource(id = R.string.queue_list_name),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium
    )

}

@Composable
private fun VoteListText() {

    Text(
        text = stringResource(id = R.string.vote_list_name),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium
    )

}

@Composable
private fun TrackControlBar(controlViewModel: ControlViewModel) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(1f)) {
            PlayButton(controlViewModel)
        }

        Box(modifier = Modifier.weight(1f)) {
            SkipButton(controlViewModel)
        }

        Box(modifier = Modifier.weight(7f)) {
            TrackSlider(controlViewModel)
        }

    }

}

@Composable
private fun PlayButton(controlViewModel: ControlViewModel) {

    IconButton(
        onClick = { controlViewModel.onTogglePause() },
        enabled = controlViewModel.isTogglePauseAvailable(),
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = if (!controlViewModel.isPaused()) painterResource(id = R.drawable.baseline_pause_24)
            else painterResource(id = R.drawable.baseline_play_arrow_24),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        )
    }

}

@Composable
private fun SkipButton(controlViewModel: ControlViewModel) {

    IconButton(
        onClick = { controlViewModel.onSkip() },
        enabled = controlViewModel.isSkipAvailable(),
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_skip_next_24),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        )
    }

}

@Composable
private fun TrackSlider(controlViewModel: ControlViewModel) {

    Slider(
        value = controlViewModel.getTrackSliderPosition(),
        onValueChange = { controlViewModel.onTrackSliderPositionChange(it) },
        onValueChangeFinished = { controlViewModel.onTrackSliderFinish() },
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )

}

@Composable
private fun SearchButton(controlViewModel: ControlViewModel, navController: NavController) {

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { controlViewModel.onSearchTracks(navController) },
        shape = RoundedCornerShape(10.dp)
    ) {

        Box(modifier = Modifier.weight(1f)) {

            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
            )

        }

        Box(modifier = Modifier.weight(9f)) {

            Text(
                text = stringResource(id = R.string.track_search_button_text),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )

        }

    }

}

@Composable
private fun TerminateSessionDialog(
    controlViewModel: ControlViewModel,
    navController: NavController
) {

    AlertDialog(
        title = { Text(text = stringResource(id = R.string.terminate_session_text)) },
        text = { Text(text = stringResource(id = R.string.terminate_session_confirmation_text)) },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = { controlViewModel.onConfirmDeleteSession(navController) }
            ) {
                Text(stringResource(id = R.string.confirmation_text))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { controlViewModel.onDismissDeleteSession(navController) }
            ) {
                Text(stringResource(id = R.string.decline_text))
            }
        }
    )

}
