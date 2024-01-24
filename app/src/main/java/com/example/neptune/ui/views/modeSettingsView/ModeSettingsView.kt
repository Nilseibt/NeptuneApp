package com.example.neptune.ui.views.modeSettingsView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun ModeSettingsView(navController: NavController) {

    val modeSettingsViewModel = viewModel<ModeSettingsViewModel>(
        factory = viewModelFactory {
            ModeSettingsViewModel(
                NeptuneApp.model.appState
            )
        }
    )

    BackHandler {
        modeSettingsViewModel.onBack(navController)
    }

    Column {

        if (modeSettingsViewModel.isPlaylistLinkInputAvailable()) {
            Text(text = "Standardplaylist", color = Color.White)
            TextField(
                value = modeSettingsViewModel.getCurrentPlaylistLinkInput(),
                onValueChange = { modeSettingsViewModel.onPlaylistLinkInputChange(it) })
        }

        if (modeSettingsViewModel.isArtistSession()) {
            Button(
                onClick = { modeSettingsViewModel.onArtistSearch(navController) }
            ) {
                Text("Artists suchen")
            }

            modeSettingsViewModel.getSelectedEntities().forEach {
                Button(onClick = { modeSettingsViewModel.onToggleSelect(it) }) {
                    Text(text = it)
                }
            }
        }

        if (modeSettingsViewModel.isGenreSession()) {
            Button(
                onClick = { modeSettingsViewModel.onGenreSearch(navController) }
            ) {
                Text("Genres suchen")
            }

            modeSettingsViewModel.getSelectedEntities().forEach {
                Button(onClick = { modeSettingsViewModel.onToggleSelect(it) }) {
                    Text(text = it)
                }
            }
        }

        Text(text = "Track Cooldown", color = Color.White)
        Slider(
            value = modeSettingsViewModel.getCooldownSliderPosition(),
            onValueChange = { modeSettingsViewModel.onCooldownSliderPositionChange(it) },
            onValueChangeFinished = { modeSettingsViewModel.onCooldownSliderFinish() })
        Text(text = modeSettingsViewModel.getTrackCooldownString())
        Button(
            onClick = { modeSettingsViewModel.onConfirmSettings(navController) }
        ) {
            Text("Bestätigen")
        }
    }
}