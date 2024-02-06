package com.example.neptune.ui.views.voteView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.commons.SessionInfoBar
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.commons.TrackListComposable
import com.example.neptune.ui.commons.TrackListType
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
import kotlinx.coroutines.delay

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

    NeptuneTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {

            TopBar(onBack = { voteViewModel.onBack(navController) })

            SessionInfoBar(
                onStatistics = { voteViewModel.onOpenStats(navController) },
                onInfo = { voteViewModel.onOpenInfo(navController) },
                description = voteViewModel.getTopBarDescription()
            )

            Box(modifier = Modifier.weight(9f)) {
                TrackListComposable(
                    tracks = voteViewModel.getVoteList(),
                    trackListType = TrackListType.PARTICIPANT_VOTE,
                    onToggleUpvote = { voteViewModel.onToggleUpvote(it) })
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = { voteViewModel.onSearchTracks(navController) }
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

        if(voteViewModel.isLeaveSessionDialogShown()) {

            AlertDialog(
                title = { Text(text = stringResource(id = R.string.leave_session_text)) },
                text = { Text(text = stringResource(id = R.string.leave_session_confirmation_text)) },
                onDismissRequest = { },
                confirmButton = {
                    TextButton(
                        onClick = { voteViewModel.onConfirmLeaveSession(navController) }
                    ) {
                        Text(text = stringResource(id = R.string.confirmation_text))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { voteViewModel.onDismissLeaveSession(navController) }
                    ) {
                        Text(text = stringResource(id = R.string.decline_text))
                    }
                }
            )
        }

        LaunchedEffect(
            key1 = Unit,
            block = {
            while (true) {
                voteViewModel.syncState()
                delay(5000)
            }
        }
        )
    }
}
