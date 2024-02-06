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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
import com.example.neptune.ui.theme.SpotifyBrandGreen
@Composable
fun StartView(navController: NavController, activity: MainActivity) {

    val startViewModel = viewModel<StartViewModel>(
        factory = viewModelFactory {
            StartViewModel(
                NeptuneApp.model.appState,
                navController,
                activity
            )
        }
    )

    BackHandler {
        startViewModel.onBack(navController)
    }

    NeptuneTheme {
        Surface(
            //modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(modifier = Modifier
                .fillMaxSize()
            ) {

                TopBar(onBack = { startViewModel.onBack(navController) })

            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(onClick = { startViewModel.onJoinSession(navController) }) {
                    Text(text = stringResource(id = R.string.join_session))
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { startViewModel.onCreateSession(navController) },
                    enabled = startViewModel.createSessionPossible()
                ) {
                    Text(text = stringResource(id = R.string.create_session))
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { startViewModel.onToggleConnectedToSpotify() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SpotifyBrandGreen
                    )
                ) {
                    Text(text = startViewModel.getSpotifyButtonText())
                }
            }


            if(startViewModel.isLeaveDialogShown()) {

                AlertDialog(
                    title = {
                        Text(text = stringResource(id = R.string.leave_application))
                    },
                    text = {
                        Text(text = stringResource(id = R.string.sure_about_leaving_application))
                    },
                    onDismissRequest = { },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                startViewModel.onConfirmLeave(navController)
                            }
                        ) {
                            Text(text = stringResource(id = R.string.app_leave_confirmation))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                startViewModel.onDismissLeave(navController)
                            }
                        ) {
                            Text(text = stringResource(id = R.string.app_leave_declination))
                        }
                    }
                )
            }
        }
    }
}