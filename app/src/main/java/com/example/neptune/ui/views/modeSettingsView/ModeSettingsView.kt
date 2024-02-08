package com.example.neptune.ui.views.modeSettingsView

import NeptuneOutlinedTextField
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.commons.TopBar
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
                val searchSliderDistance = 16.dp
                val iconTextDistance = 8.dp

                if (modeSettingsViewModel.isPlaylistLinkInputAvailable()) {
                    //Text(text = "Standardplaylist", color = Color.White)
                    /*OutlinedTextField(
                        value = modeSettingsViewModel.getCurrentPlaylistLinkInput(),
                        onValueChange = { modeSettingsViewModel.onPlaylistLinkInputChange(it) },
                        label = { Text(text = stringResource(id = R.string.default_playlist), color = Color.White) }
                    )*/
                    
                    NeptuneOutlinedTextField(modeSettingsViewModel = modeSettingsViewModel, labelText = stringResource(
                        id = R.string.default_playlist
                    ))

                    //TODO change color and make string resource
                    if(!modeSettingsViewModel.isPlaylistLinkValid()){
                        Text(text = "Playlist link invalid", color = Color.Red)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (modeSettingsViewModel.isArtistSession()) {
                    FilledTonalButton(
                        onClick = { modeSettingsViewModel.onArtistSearch(navController) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //Text("Artists suchen")
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(iconTextDistance))
                        Text(text = stringResource(id = R.string.artist_search))
                    }
                    Spacer(modifier = Modifier.height(searchSliderDistance))

                    val itemList = modeSettingsViewModel.getSelectedEntities()

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(itemList) { item ->
                            // your composable here
                            ElevatedButton(onClick = { modeSettingsViewModel.onToggleSelect(item) }) {
                                Text(text = item)
                                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    }

                    /** modeSettingsViewModel.getSelectedEntities().forEach {
                        Button(onClick = { modeSettingsViewModel.onToggleSelect(it) }) {
                            Text(text = it)
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                    */
                }

                if (modeSettingsViewModel.isGenreSession()) {
                    FilledTonalButton(
                        onClick = { modeSettingsViewModel.onGenreSearch(navController) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //Text("Genres suchen")
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(id = R.string.genre_search))
                    }
                    Spacer(modifier = Modifier.height(searchSliderDistance))

                    val itemList = modeSettingsViewModel.getSelectedEntities()
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(itemList) { item ->
                            // your composable here
                            ElevatedButton(onClick = { modeSettingsViewModel.onToggleSelect(item) }) {
                                Text(text = item)
                                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    }

                    /**
                    modeSettingsViewModel.getSelectedEntities().forEach {
                        Button(onClick = { modeSettingsViewModel.onToggleSelect(it) }) {
                            Text(text = it)
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                    */
                }

                //Text(text = "Track Cooldown", color = Color.White)

                Spacer(modifier = Modifier.height(searchSliderDistance))

                Text(text = stringResource(id = R.string.track_cooldown_text))
                Slider(
                    value = modeSettingsViewModel.getCooldownSliderPosition(),
                    onValueChange = { modeSettingsViewModel.onCooldownSliderPositionChange(it) },
                    onValueChangeFinished = { modeSettingsViewModel.onCooldownSliderFinish() })
                Text(text = modeSettingsViewModel.getTrackCooldownString())
                
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
                Button(
                    onClick = {
                        if (confirmButtonEnabled) {
                            confirmButtonEnabled = false
                            modeSettingsViewModel.onConfirmSettings(navController)
                        }
                    }
                ) {
                    //Text("Bestätigen")
                    Text(text = stringResource(id = R.string.confirmation_button_text))
                }
            }
        }
    }
}



