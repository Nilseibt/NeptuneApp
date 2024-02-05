package com.example.neptune.ui.views.controlView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.user.src.Host
import com.example.neptune.ui.commons.SessionInfoBar
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.commons.TrackListComposable
import com.example.neptune.ui.commons.TrackListType
import com.example.neptune.ui.views.util.viewModelFactory
import kotlinx.coroutines.delay

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

        Text(
            text = stringResource(id = R.string.queue_list_name),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Box(modifier = Modifier.weight(7f)) {

            TrackListComposable(
                tracks = controlViewModel.getQueueList(),
                trackListType = TrackListType.HOST_QUEUE,
                onToggleUpvote = { controlViewModel.onToggleUpvote(it) },
                onToggleDropdown = { controlViewModel.onToggleDropdownQueue(it) },
                isDropdownExpanded = { controlViewModel.isDropdownExpandedQueue(it) },
                onRemoveFromQueue = {controlViewModel.onRemoveFromQueue(it) },
                onToggleBlock = { controlViewModel.onToggleBlock(it) },
                onMoveUp = { controlViewModel.onMoveUp(it) },
                onMoveDown = { controlViewModel.onMoveDown(it) }
            )

        }

        Text(
            text = stringResource(id = R.string.vote_list_name),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = { controlViewModel.onTogglePause() },
                enabled = controlViewModel.isTogglePauseAvailable(),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if(controlViewModel.isPaused()) Icons.Filled.PlayArrow else Icons.Filled.Clear,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(
                onClick = { controlViewModel.onSkip() },
                enabled = controlViewModel.isSkipAvailable(),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Slider(
                value = controlViewModel.getTrackSliderPosition(),
                onValueChange = { controlViewModel.onTrackSliderPositionChange(it) },
                onValueChangeFinished = { controlViewModel.onTrackSliderFinish() },
                modifier = Modifier.weight(7f)
            )

        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f),
            onClick = { controlViewModel.onSearchTracks(navController) }
        ) {

            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(id = R.string.track_search_button_text),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .weight(9f)
                    .padding(3.dp),
                textAlign = TextAlign.Center
            )

        }

    }

    if(controlViewModel.isDeleteSessionDialogShown()) {

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

    LaunchedEffect(key1 = Unit, block = {
        while (true) {
            controlViewModel.syncState()
            delay(3000)
        }
    })
}

/*
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

    Column {

        Button(onClick = { controlViewModel.onOpenInfo(navController) }) {
            Text("Info öffnen (Icon)")
        }

        Text("TopBar Beschr.: " + controlViewModel.getTopBarDescription())

        Button(onClick = { controlViewModel.onOpenStats(navController) }) {
            Text("Statistiken öffnen (Icon)")
        }

        Text(text = "Queue", color = Color.White)
        Box(modifier = Modifier.weight(7f)) {
            TrackListComposable(
                tracks = controlViewModel.getQueueList(),
                trackListType = TrackListType.HOST_QUEUE,
                onToggleUpvote = { controlViewModel.onToggleUpvote(it) },
                onToggleDropdown = { controlViewModel.onToggleDropdownQueue(it) },
                isDropdownExpanded = { controlViewModel.isDropdownExpandedQueue(it) },
                onRemoveFromQueue = {controlViewModel.onRemoveFromQueue(it) },
                onToggleBlock = { controlViewModel.onToggleBlock(it) },
                onMoveUp = { controlViewModel.onMoveUp(it) },
                onMoveDown = { controlViewModel.onMoveDown(it) })
        }

        Text(text = "Upvote Liste", color = Color.White)
        Box(modifier = Modifier.weight(7f)) {
            TrackListComposable(
                tracks = controlViewModel.getVoteList(),
                trackListType = TrackListType.HOST_VOTE,
                onToggleUpvote = { controlViewModel.onToggleUpvote(it) },
                onToggleDropdown = { controlViewModel.onToggleDropdownVote(it) },
                isDropdownExpanded = { controlViewModel.isDropdownExpandedVote(it) },
                onAddToQueue = { controlViewModel.onAddToQueue(it) },

                onToggleBlock = { controlViewModel.onToggleBlock(it) })
        }
        Button(onClick = { controlViewModel.onTogglePause() }, enabled = controlViewModel.isTogglePauseAvailable()){
            Text(text = controlViewModel.getPausedDescription())
        }
        Button(onClick = { controlViewModel.onSkip() }, enabled = controlViewModel.isSkipAvailable()) {
            Text(text = "Überspringen")
        }
        Slider(
            value = controlViewModel.getTrackSliderPosition(),
            onValueChange = { controlViewModel.onTrackSliderPositionChange(it) },
            onValueChangeFinished = { controlViewModel.onTrackSliderFinish() })
        Button(onClick = { controlViewModel.onSearchTracks(navController) }){
            Text(text = "Tracks suchen")
        }
    }


    if(controlViewModel.isDeleteSessionDialogShown()) {

        AlertDialog(
            title = {
                Text(text = "Session beenden")
            },
            text = {
                Text(text = "Sicher, dass du die Session für alle beenden willst?")
            },
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        controlViewModel.onConfirmDeleteSession(navController)
                    }
                ) {
                    Text("Ja")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        controlViewModel.onDismissDeleteSession(navController)
                    }
                ) {
                    Text("Nein")
                }
            }
        )
    }

    LaunchedEffect(key1 = Unit, block = {
        while (true) {
            controlViewModel.syncState()
            delay(3000)
        }
    })
 */
