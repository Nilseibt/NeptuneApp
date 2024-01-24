package com.example.neptune.ui.views.startView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun StartView(navController: NavController) {

    val startViewModel = viewModel<StartViewModel>(
        factory = viewModelFactory {
            StartViewModel(
                NeptuneApp.model.appState
            )
        }
    )

    BackHandler {
        startViewModel.onBack(navController)
    }

    Column {
        Button(onClick = { startViewModel.onJoinSession(navController) }) {
            Text(text = "Session beitreten")
        }
        Button(
            onClick = { startViewModel.onCreateSession(navController) },
            enabled = startViewModel.createSessionPossible()
        ) {
            Text(text = "Session erstellen")
        }
        Button(onClick = { startViewModel.onToggleConnectedToSpotify() }) {
            Text(text = startViewModel.getSpotifyButtonText())
        }
    }


    if(startViewModel.isLeaveDialogShown()) {

        AlertDialog(
            title = {
                Text(text = "App verlassen")
            },
            text = {
                Text(text = "Sicher, dass du die App verlassen willst?")
            },
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        startViewModel.onConfirmLeave(navController)
                    }
                ) {
                    Text("Ja")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        startViewModel.onDismissLeave(navController)
                    }
                ) {
                    Text("Nein")
                }
            }
        )
    }
}







