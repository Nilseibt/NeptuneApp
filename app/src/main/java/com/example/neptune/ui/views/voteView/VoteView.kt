package com.example.neptune.ui.views.voteView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.commons.TrackListComposable
import com.example.neptune.ui.commons.TrackListType
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun VoteView(navController: NavController) {

    val voteViewModel = viewModel<VoteViewModel>(
        factory = viewModelFactory {
            VoteViewModel(
                //NeptuneApp.???
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text = "Neptune",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .weight(4f)
                .padding(1.dp)
                .align(alignment = Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically)
        ) {

        }
    }
}
