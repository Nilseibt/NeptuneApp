package com.example.neptune.ui.views.controlView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.user.src.Host
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
        Button(onClick = { controlViewModel.onSkip() }){
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
}