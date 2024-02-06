package com.example.neptune.ui.views.modeSelectView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.Blue10
import com.example.neptune.ui.theme.Blue20
import com.example.neptune.ui.theme.Blue30
import com.example.neptune.ui.theme.Blue40
import com.example.neptune.ui.theme.DarkBlue10
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.theme.SpotifyBrandGreen
import com.example.neptune.ui.views.util.viewModelFactory

val colorWhenSelected = Blue40
val colorWhenNotSelected = Color.Gray
@Composable
fun ModeSelectView(navController: NavController) {

    val modeSelectViewModel = viewModel<ModeSelectViewModel>(
        factory = viewModelFactory {
            ModeSelectViewModel(
                NeptuneApp.model.appState
            )
        }
    )

    BackHandler {
        modeSelectViewModel.onBack(navController)
    }

    fun determineButtonColor(mode: SessionType): Color {
        val isModeSelected = modeSelectViewModel.isModeSelected(mode)
        return if (isModeSelected) colorWhenSelected else colorWhenNotSelected
    }

    NeptuneTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(modifier = Modifier
                .fillMaxSize()
            ) {
                TopBar(onBack = { modeSelectViewModel.onBack(navController) })
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val generalModifier = Modifier
                            .weight(1f)
                        /*
                        if (modeSelectViewModel.isModeSelected(SessionType.GENERAL)) {

                            generalModifier.border(5.dp, Color.Black, RoundedCornerShape(50))
                        }
                        */

                        FilledTonalButton(
                            onClick = { modeSelectViewModel.onSelectMode(SessionType.GENERAL) },
                            modifier = generalModifier,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = determineButtonColor(SessionType.GENERAL)
                            )
                        ) {
                            //Text(text = "General Mode")
                            Text(text = stringResource(id = R.string.general_mode_name))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        val artistModifier = Modifier
                            .weight(1f)
                        /*
                        (modeSelectViewModel.isModeSelected(SessionType.ARTIST)) {
                            artistModifier.border(5.dp, Color.Black, RoundedCornerShape(50))
                        }
                        */
                        FilledTonalButton(
                            onClick = { modeSelectViewModel.onSelectMode(SessionType.ARTIST) },
                            modifier = artistModifier,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = determineButtonColor(SessionType.ARTIST)
                            )
                        ) {
                            //Text(text = "Artist Mode")
                            Text(text = stringResource(id = R.string.artist_mode_name))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val genreModifier = Modifier
                            .weight(1f)
                        /*
                        if (modeSelectViewModel.isModeSelected(SessionType.GENRE)) {
                            genreModifier.border(5.dp, Color.Black, RoundedCornerShape(50))
                        }
                        */
                        FilledTonalButton(
                            onClick = { modeSelectViewModel.onSelectMode(SessionType.GENRE) },
                            modifier = genreModifier,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = determineButtonColor(SessionType.GENRE)
                            )
                        ) {
                            //Text(text = "Genre Mode")
                            Text(text = stringResource(id = R.string.genre_mode_name))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        val playlistModifier = Modifier
                            .weight(1f)
                        /*
                        if (modeSelectViewModel.isModeSelected(SessionType.PLAYLIST)) {
                            playlistModifier.border(5.dp, Color.Black, RoundedCornerShape(50))
                        }
                        */
                        FilledTonalButton(
                            onClick = { modeSelectViewModel.onSelectMode(SessionType.PLAYLIST) },
                            modifier = playlistModifier,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = determineButtonColor(SessionType.PLAYLIST)
                            )
                        ) {
                            //Text(text = "Playlist Mode")
                            Text(text = stringResource(id = R.string.playlist_mode_name))
                        }
                    }

                }



                Spacer(modifier = Modifier.height(40.dp))

                Box(
                    modifier = Modifier
                        //.border(1.dp, Color.White, RoundedCornerShape(1))
                        //.padding(8.dp)
                        .size(300.dp, 100.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = modeSelectViewModel.getSelectedModeDescription(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))

                Button(onClick = { modeSelectViewModel.onConfirmMode(navController) }
                ) {
                    //Text(text = "Modus Bestätigen")
                    Text(text = stringResource(id = R.string.confirmation_button_text))
                }
            }
        }
    }
}