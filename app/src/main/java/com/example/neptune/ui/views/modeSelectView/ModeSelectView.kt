package com.example.neptune.ui.views.modeSelectView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.neptune.NeptuneApp.Companion.context
import com.example.neptune.R
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory

/**
 * The composable for the ModeSelectView.
 *
 * @param navController The NavController needed to navigate to another view.
 */
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

    NeptuneTheme {
        ModeSelectViewContent(modeSelectViewModel = modeSelectViewModel, navController = navController )
    }
}

@Composable
private fun ModeSelectViewContent(modeSelectViewModel: ModeSelectViewModel, navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center) {

            TopBar(onBack = { modeSelectViewModel.onBack(navController) })

            Column(modifier = Modifier
                .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val modeButtonModifier = Modifier
                    .weight(1f)
                    .padding(8.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    ModeButton(
                        mode = SessionType.GENERAL,
                        modeSelectViewModel = modeSelectViewModel,
                        modifier = modeButtonModifier
                    )

                    ModeButton(
                        mode = SessionType.ARTIST,
                        modeSelectViewModel = modeSelectViewModel,
                        modifier = modeButtonModifier
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {


                    ModeButton(
                        mode = SessionType.GENRE,
                        modeSelectViewModel = modeSelectViewModel,
                        modifier = modeButtonModifier
                    )

                    ModeButton(
                        mode = SessionType.PLAYLIST,
                        modeSelectViewModel = modeSelectViewModel,
                        modeButtonModifier
                    )
                }

                Row(modifier = Modifier
                    .weight(2f)) {

                    ModeInfoCard(modeSelectViewModel)

                }

                Row(modifier = Modifier
                    .weight(1f),
                ) {
                    Button(onClick = { modeSelectViewModel.onConfirmMode(navController) }
                    ) {
                        Text(text = stringResource(id = R.string.confirmation_button_text))
                    }
                }
            }
        }
    }
}

/**
 * The Composable displayed to the User to select a Mode.
 *
 * @param mode Defines the SessionType the Button is used for
 * @param modeSelectViewModel The ViewModel the Button executes methods on
 * @param modifier The modifier specified for the Button
 */
@Composable
private fun ModeButton(
    mode: SessionType,
    modeSelectViewModel: ModeSelectViewModel,
    modifier : Modifier
) {
    val colorWhenSelected = MaterialTheme.colorScheme.secondary
    val buttonColor: Color = getButtonColor(mode, modeSelectViewModel, colorWhenSelected)

    FilledTonalButton(
        onClick = { modeSelectViewModel.onSelectMode(mode) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        Text(text = getModeButtonText(mode))
    }
}

@Composable
private fun ModeInfoCard(modeSelectViewModel: ModeSelectViewModel) {
    Card(modifier = Modifier
        .padding(32.dp)) {
        Text(
            text = modeSelectViewModel.getSelectedModeDescription(),
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            textAlign = TextAlign.Center
        )
    }
}

private fun getButtonColor(mode : SessionType, modeSelectViewModel : ModeSelectViewModel, colorWhenSelected : Color) : Color {
    val colorWhenNotSelected = Color.Gray

    val isModeSelected = modeSelectViewModel.isModeSelected(mode)

    return if (isModeSelected) colorWhenSelected else colorWhenNotSelected
}

private fun getModeButtonText(mode: SessionType): String {
    return when (mode) {
        SessionType.ARTIST -> context.getString(R.string.artist_mode_name)
        SessionType.GENRE -> context.getString(R.string.genre_mode_name)
        SessionType.PLAYLIST -> context.getString(R.string.playlist_mode_name)
        else -> context.getString(R.string.general_mode_name)
    }
}