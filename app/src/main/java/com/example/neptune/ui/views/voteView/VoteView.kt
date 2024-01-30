package com.example.neptune.ui.views.voteView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.commons.SessionInfoBar
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.commons.TrackListComposable
import com.example.neptune.ui.commons.TrackListType
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun VoteView(navController: NavController) {

    val voteViewModel = viewModel<VoteViewModel>(
        factory = viewModelFactory {
            VoteViewModel(
                NeptuneApp.model.user!!
            )
        }
    )

    BackHandler {
        voteViewModel.onBack(navController)
    }

    Column {
        Box(modifier = Modifier.weight(7f)) {
            TrackListComposable(
                tracks = voteViewModel.getVoteList(),
                trackListType = TrackListType.PARTICIPANT_VOTE,
                onToggleUpvote = { voteViewModel.onToggleUpvote(it) })
        }
        Button(onClick = { voteViewModel.onSearchTracks(navController) }){
            Text(text = "Tracks suchen")
        }
    }

    if(voteViewModel.isLeaveSessionDialogShown()) {

        AlertDialog(
            title = {
                Text(text = "Session verlassen")
            },
            text = {
                Text(text = "Sicher, dass du die Session verlassen willst?")
            },
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        voteViewModel.onConfirmLeaveSession(navController)
                    }
                ) {
                    Text("Ja")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        voteViewModel.onDismissLeaveSession(navController)
                    }
                ) {
                    Text("Nein")
                }
            }
        )
    }
}

@Preview(name = "Vote View Preview")
@Composable
fun VoteViewPreview() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background))

    Column {
        TopBar { }
        SessionInfoBar(onStatistics = { /*TODO*/ }, onInfo = { /*TODO*/ }, title = "General Modus")
    }


}
