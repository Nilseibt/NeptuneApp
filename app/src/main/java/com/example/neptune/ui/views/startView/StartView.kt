package com.example.neptune.ui.views.startView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
@Composable
fun StartView(navController: NavController) {
    NeptuneTheme {
        val startViewModel = viewModel<StartViewModel>(
            factory = viewModelFactory {
                StartViewModel(
                    NeptuneApp.model.appState,
                    navController
                )
            }
        )

        BackHandler {
            startViewModel.onBack(navController)
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { startViewModel.onJoinSession(navController) }) {
                //Text(text = "Session beitreten")
                Text(text = stringResource(id = R.string.join_session))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { startViewModel.onCreateSession(navController) },
                enabled = startViewModel.createSessionPossible()
            ) {
                //Text(text = "Session erstellen")
                Text(text = stringResource(id = R.string.create_session))
            }
            Spacer(modifier = Modifier.height(32.dp))
            FilledTonalButton(onClick = { startViewModel.onToggleConnectedToSpotify() }) {
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


}







