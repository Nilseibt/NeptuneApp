package com.example.neptune.ui.views.modeSettingsView

import NeptuneOutlinedTextField
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.InvalidInputWarningColor
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
import kotlinx.coroutines.delay

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

    NeptuneTheme {
        ModeSettingsViewContent(modeSettingsViewModel = modeSettingsViewModel, navController = navController)
    }
}

@Composable
private fun ModeSettingsViewContent(modeSettingsViewModel: ModeSettingsViewModel, navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {

        Column(modifier = Modifier
            .fillMaxSize()
        ) {

            TopBar(onBack = { modeSettingsViewModel.onBack(navController) })

        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AdditionalModeContent(modeSettingsViewModel = modeSettingsViewModel, navController = navController)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(id = R.string.track_cooldown_text))
            TrackCooldownSlider(modeSettingsViewModel = modeSettingsViewModel)

            Spacer(modifier = Modifier.height(40.dp))

            // This is a guard for prohibiting double clicking the confirm button
            var confirmButtonEnabled by remember { mutableStateOf(true) }
            LaunchedEffect(confirmButtonEnabled) {
                if (confirmButtonEnabled) {
                    return@LaunchedEffect
                } else {
                    delay(1000)
                    confirmButtonEnabled = true
                }
            }
            ConfirmButton(modeSettingsViewModel = modeSettingsViewModel, navController = navController, enabled = confirmButtonEnabled)
        }
    }
}


@Composable
private fun AdditionalModeContent(modeSettingsViewModel: ModeSettingsViewModel, navController: NavController) {
    if (modeSettingsViewModel.isPlaylistLinkInputAvailable()) {

        PlaylistModeContent(modeSettingsViewModel = modeSettingsViewModel)

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (modeSettingsViewModel.isArtistSession()) {

        ArtistSearchContent(modeSettingsViewModel = modeSettingsViewModel, navController = navController)

    }

    if (modeSettingsViewModel.isGenreSession()) {

        GenreSearchContent(modeSettingsViewModel = modeSettingsViewModel, navController = navController)

    }
}

@Composable
private fun PlaylistModeContent(modeSettingsViewModel: ModeSettingsViewModel) {
    NeptuneOutlinedTextField(
        modeSettingsViewModel = modeSettingsViewModel,
        labelText = stringResource(id = R.string.default_playlist)
    )

    if(!modeSettingsViewModel.isPlaylistLinkValid()){
        Text(text = stringResource(id = R.string.playlist_link_invalid), color = InvalidInputWarningColor)
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun ArtistSearchContent(modeSettingsViewModel: ModeSettingsViewModel, navController: NavController) {

    ArtistSearchFieldButton(modeSettingsViewModel = modeSettingsViewModel, navController = navController)

    Spacer(modifier = Modifier.height(16.dp))

    SelectedEntitiesLazyRow(modeSettingsViewModel = modeSettingsViewModel)
}

@Composable
private fun ArtistSearchFieldButton(modeSettingsViewModel: ModeSettingsViewModel, navController: NavController) {
    FilledTonalButton(
        onClick = { modeSettingsViewModel.onArtistSearch(navController) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(id = R.string.artist_search))
    }}

@Composable
private fun SelectedEntitiesLazyRow(modeSettingsViewModel: ModeSettingsViewModel) {
    val itemList = modeSettingsViewModel.getSelectedEntities()
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(itemList) { item ->
            ElevatedButton(onClick = { modeSettingsViewModel.onToggleSelect(item) }) {
                Text(text = item)
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        }
    }
}

@Composable
private fun GenreSearchContent(modeSettingsViewModel: ModeSettingsViewModel, navController: NavController) {
    GenreSearchFieldButton(modeSettingsViewModel = modeSettingsViewModel, navController = navController)

    Spacer(modifier = Modifier.height(16.dp))

    SelectedEntitiesLazyRow(modeSettingsViewModel = modeSettingsViewModel)
}

@Composable
private fun GenreSearchFieldButton(modeSettingsViewModel: ModeSettingsViewModel, navController: NavController) {
    FilledTonalButton(
        onClick = { modeSettingsViewModel.onGenreSearch(navController) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(id = R.string.genre_search))
    }
}

@Composable
private fun TrackCooldownSlider(modeSettingsViewModel: ModeSettingsViewModel) {
    Slider(
        value = modeSettingsViewModel.getCooldownSliderPosition(),
        onValueChange = { modeSettingsViewModel.onCooldownSliderPositionChange(it) },
        onValueChangeFinished = { modeSettingsViewModel.onCooldownSliderFinish() })
    Text(text = modeSettingsViewModel.getTrackCooldownString())
}

@Composable
private fun ConfirmButton(modeSettingsViewModel: ModeSettingsViewModel, navController: NavController, enabled: Boolean) {
    var buttonEnabled = enabled
    Button(
        onClick = {
            if (buttonEnabled) {
                buttonEnabled = false
                modeSettingsViewModel.onConfirmSettings(navController)
            }
        }
    ) {
        Text(text = stringResource(id = R.string.confirmation_button_text))
    }
}


