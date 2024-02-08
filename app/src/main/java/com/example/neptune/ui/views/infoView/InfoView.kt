package com.example.neptune.ui.views.infoView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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

/**
 * The composable for the infoView.
 *
 * @param navController the NavController needed to navigate to another view
 */
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

        InfoViewContent(infoViewModel, navController)

    }

}

@Composable
private fun InfoViewContent(infoViewModel: InfoViewModel, navController: NavController) {

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {

        TopBar(onBack = { infoViewModel.onBack(navController) })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            Box(modifier = Modifier.weight(2f)) {
                SessionModeText(infoViewModel)
            }

            if (infoViewModel.isGenreMode()) {
                Box(modifier = Modifier.weight(2f)) {
                    GenreDescription(infoViewModel)
                }
            }

            if (infoViewModel.isArtistMode()) {
                Box(modifier = Modifier.weight(2f)) {
                    ArtistDescription(infoViewModel)
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                SessionCodeText()
            }

            Box(modifier = Modifier.weight(6f)) {
                QRCodeImage(infoViewModel)
            }

            Box(modifier = Modifier.weight(1f)) {
                SessionCode(infoViewModel)
            }

        }

    }

}

@Composable
private fun SessionModeText(infoViewModel: InfoViewModel) {

    Row(
        modifier = Modifier.fillMaxWidth(),
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

}

@Composable
private fun GenreDescription(infoViewModel: InfoViewModel) {

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Text(
            text = stringResource(id = R.string.genre_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )

        LazyRow {
            items(infoViewModel.getAllGenres()) { item ->
                TextButton(onClick = {  }) {
                    Text(
                        text = item,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

    }

}

@Composable
private fun ArtistDescription(infoViewModel: InfoViewModel) {

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Text(
            text = stringResource(id = R.string.artists_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )

        LazyRow {
            items(infoViewModel.getAllArtists()) { item ->
                TextButton(onClick = {  }) {
                    Text(
                        text = item,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

    }

}

@Composable
private fun SessionCodeText() {

    Row(
        modifier = Modifier.fillMaxWidth(),
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

}

@Composable
private fun QRCodeImage(infoViewModel: InfoViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            bitmap = infoViewModel.getQRCode(),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        )

    }

}

@Composable
private fun SessionCode(infoViewModel: InfoViewModel) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(1f)) {
            SessionCodeAsText(infoViewModel)
        }

        Box(modifier = Modifier.weight(1f)) {
            SessionCodeShareButton(infoViewModel)
        }

    }

}

@Composable
private fun SessionCodeAsText(infoViewModel: InfoViewModel) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = infoViewModel.getSessionCode(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(2.dp)
        )

    }

}

@Composable
private fun SessionCodeShareButton(infoViewModel: InfoViewModel) {

    Button(
        onClick = { infoViewModel.onShareLink() },
        modifier = Modifier.fillMaxWidth()
    ) {

        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = "",
            modifier = Modifier.weight(1f),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            text = stringResource(id = R.string.share_text),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(4f),
            textAlign = TextAlign.Center
        )

    }

}
