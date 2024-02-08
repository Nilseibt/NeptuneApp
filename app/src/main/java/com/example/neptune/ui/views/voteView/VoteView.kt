package com.example.neptune.ui.views.voteView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

/**
 * The composable for the voteView.
 *
 * @param navController the NavController needed to navigate to another view
 */
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

    LaunchedEffect(
        key1 = Unit,
        block = {
            while (true) {
                voteViewModel.syncState(navController)
                delay(5000)
            }
        }
    )

    NeptuneTheme {

        VoteViewContent(voteViewModel, navController)

    }

}

@Composable
private fun VoteViewContent(voteViewModel: VoteViewModel, navController: NavController) {

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

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {

            Box(modifier = Modifier.weight(9f)) {
                TrackListComposable(
                    tracks = voteViewModel.getVoteList(),
                    trackListType = TrackListType.PARTICIPANT_VOTE,
                    onToggleUpvote = { voteViewModel.onToggleUpvote(it) }
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                SearchButton(voteViewModel, navController)
            }

        }

    }

    if (voteViewModel.isLeaveSessionDialogShown()) {
        LeaveDialog(voteViewModel, navController)
    }

}

@Composable
private fun SearchButton(voteViewModel: VoteViewModel, navController: NavController) {

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { voteViewModel.onSearchTracks(navController) },
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
private fun LeaveDialog(voteViewModel: VoteViewModel, navController: NavController) {

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
