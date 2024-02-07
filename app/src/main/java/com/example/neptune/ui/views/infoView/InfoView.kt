package com.example.neptune.ui.views.infoView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun InfoView(navController: NavController, activity: MainActivity) {

    val infoViewModel = viewModel<InfoViewModel>(
        factory = viewModelFactory {
            InfoViewModel(
                NeptuneApp.model.user!!,
                activity
            )
        }
    )

    BackHandler {
        infoViewModel.onBack(navController)
    }

    NeptuneTheme {

        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {

            TopBar(onBack = { infoViewModel.onBack(navController) })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .background(color = MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val sessionMode = when(infoViewModel.getMode()){
                    SessionType.GENERAL -> stringResource(id = R.string.general_mode_name)
                    SessionType.ARTIST -> stringResource(id = R.string.artist_mode_name)
                    SessionType.GENRE -> stringResource(id = R.string.genre_mode_name)
                    SessionType.PLAYLIST -> stringResource(id = R.string.playlist_mode_name)
                }
                Text(
                    text = sessionMode,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(7f),
                    textAlign = TextAlign.Center
                )

            }

            if (infoViewModel.isGenreMode()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Text(
                        text = stringResource(id = R.string.genre_text),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge
                    )

                    LazyRow {
                        items(infoViewModel.getAllGenres()) { item ->
                            ElevatedButton(onClick = {  }) {
                                Text(text = item)
                            }
                        }
                    }
                }

            }

            if (infoViewModel.isArtistMode()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Text(
                        text = stringResource(id = R.string.artists_text),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge
                    )

                    LazyRow {
                        items(infoViewModel.getAllArtists()) { item ->
                            ElevatedButton(onClick = {  }) {
                                Text(text = item)
                            }
                        }
                    }
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.session_code_text),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5f),
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    bitmap = infoViewModel.getQRCode(),
                    contentDescription = "QRCode to join session",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Text(
                    text = infoViewModel.getSessionCode(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = { infoViewModel.onShareLink() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "",
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = stringResource(id = R.string.share_text),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(4f),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

    }
}
