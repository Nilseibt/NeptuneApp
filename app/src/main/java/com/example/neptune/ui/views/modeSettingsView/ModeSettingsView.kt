package com.example.neptune.ui.views.modeSettingsView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)

    ) {

        if (modeSettingsViewModel.isPlaylistLinkInputAvailable()) {
            //Text(text = "Standardplaylist", color = Color.White)
            Text(text = stringResource(id = R.string.default_playlist), color = Color.White)
            TextField(
                value = modeSettingsViewModel.getCurrentPlaylistLinkInput(),
                onValueChange = { modeSettingsViewModel.onPlaylistLinkInputChange(it) })
        }

        if (modeSettingsViewModel.isArtistSession()) {
            Button(
                onClick = { modeSettingsViewModel.onArtistSearch(navController) }
            ) {
                //Text("Artists suchen")
                Text(text = stringResource(id = R.string.artist_search))
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
                //Text("Genres suchen")
                Text(text = stringResource(id = R.string.genre_search))
            }

            modeSettingsViewModel.getSelectedEntities().forEach {
                Button(onClick = { modeSettingsViewModel.onToggleSelect(it) }) {
                    Text(text = it)
                }
            }
        }

        //Text(text = "Track Cooldown", color = Color.White)
        Text(text = stringResource(id = R.string.track_cooldown_text), color = Color.White)
        Slider(
            value = modeSettingsViewModel.getCooldownSliderPosition(),
            onValueChange = { modeSettingsViewModel.onCooldownSliderPositionChange(it) },
            onValueChangeFinished = { modeSettingsViewModel.onCooldownSliderFinish() })
        Text(text = modeSettingsViewModel.getTrackCooldownString())
        Button(
            onClick = { modeSettingsViewModel.onConfirmSettings(navController) }
        ) {
            //Text("Bestätigen")
            Text(text = stringResource(id = R.string.confirmation_button_text))
        }
    }
}