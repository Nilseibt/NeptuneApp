package com.example.neptune.ui.views.startView

import android.graphics.fonts.FontStyle
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.ButtonBlue
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
import com.example.neptune.ui.theme.SpotifyBrandGreen

/**
 * The composable for the StartView.
 *
 * @param navController The NavController needed to navigate to another view.
 */
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
        StartViewContent(navController, startViewModel)
    }
}

@Composable
fun StartViewContent(navController: NavController, startViewModel: StartViewModel) {
    Surface(
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

            StartViewStandardButton(
                onClick = { startViewModel.onJoinSession(navController) },
                buttonText = stringResource(id = R.string.join_session),
                enabled = true,
                buttonInfo = stringResource(id = R.string.join_session_info)
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            StartViewStandardButton(
                onClick = { startViewModel.onCreateSession(navController) },
                buttonText = stringResource(id = R.string.create_session),
                enabled = startViewModel.createSessionPossible(),
                buttonInfo = stringResource(id = R.string.create_session_info)
            )

            Spacer(modifier = Modifier.height(32.dp))

            SpotifyConnectionButton(startViewModel = startViewModel)
        }

        if(startViewModel.isLeaveDialogShown()) {
            LeaveAppDialog(
                startViewModel = startViewModel,
                navController = navController
            )
        }
    }
}

@Composable
private fun StartViewStandardButton(onClick: () -> Unit, buttonText: String, enabled: Boolean, buttonInfo: String) {
    Button(
        onClick = onClick,
        border = BorderStroke(3.dp, ButtonBlue),
        enabled = enabled
    ) {
        Text(text = buttonText)
    }
    
    Text(buttonInfo, fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(255,255,255, 150))
}

@Composable
private fun SpotifyConnectionButton(startViewModel: StartViewModel) {
    Button(onClick = { startViewModel.onToggleConnectedToSpotify() },
        colors = ButtonDefaults.buttonColors(
            containerColor = SpotifyBrandGreen
        )
    ) {
        Text(text = startViewModel.getSpotifyButtonText())
    }
}

@Composable
private fun LeaveAppDialog(startViewModel: StartViewModel, navController: NavController) {
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
                Text(text = stringResource(id = R.string.application_leave_confirmation))
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