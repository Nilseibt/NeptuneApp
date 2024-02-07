package com.example.neptune.ui.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.neptune.R
import com.example.neptune.data.model.session.SessionType
import kotlinx.coroutines.delay

@Composable
fun TopBar(onBack: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .height(60.dp)
    ) {

        // This is a guard for prohibiting double clicking the back button
        var backButtonEnabled by remember { mutableStateOf(true) }
        LaunchedEffect(backButtonEnabled) {
            if (backButtonEnabled) {
                return@LaunchedEffect
            } else {
                delay(500)
                backButtonEnabled = true
            }
        }
        IconButton(
            onClick = {
                if (backButtonEnabled) {
                    backButtonEnabled = false
                    onBack()
                }
            },
            modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .weight(4f)
                .padding(4.dp)
                .align(alignment = Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.neptune_logo),
            contentDescription = "",
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        )

    }
}

@Composable
fun SessionInfoBar(onStatistics: () -> Unit, onInfo: () -> Unit, description: SessionType) {

    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .height(30.dp)
    ) {

        // This is a guard for prohibiting double clicking the info button
        var infoButtonEnabled by remember { mutableStateOf(true) }
        LaunchedEffect(infoButtonEnabled) {
            if (infoButtonEnabled) {
                return@LaunchedEffect
            } else {
                delay(1000)
                infoButtonEnabled = true
            }
        }
        IconButton(
            onClick = {
                if (infoButtonEnabled) {
                    infoButtonEnabled = false
                    onInfo()
                }
            },
            modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_groups_24),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }

        val title = when (description) {
            SessionType.GENERAL -> stringResource(id = R.string.general_mode_name)
            SessionType.ARTIST -> stringResource(id = R.string.artist_mode_name)
            SessionType.GENRE -> stringResource(id = R.string.genre_mode_name)
            SessionType.PLAYLIST -> stringResource(id = R.string.playlist_mode_name)
        }
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(5f)
                .padding(2.dp)
                .align(alignment = Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )

        // This is a guard for prohibiting double clicking the stats button
        var statsButtonEnabled by remember { mutableStateOf(true) }
        LaunchedEffect(statsButtonEnabled) {
            if (statsButtonEnabled) {
                return@LaunchedEffect
            } else {
                delay(1000)
                statsButtonEnabled = true
            }
        }
        IconButton(
            onClick = {
                if (statsButtonEnabled) {
                    statsButtonEnabled = false
                    onStatistics()
                }
            },
            modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_bar_chart_24),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }

    }
}
