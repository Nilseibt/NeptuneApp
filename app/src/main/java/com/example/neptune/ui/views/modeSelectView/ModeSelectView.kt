package com.example.neptune.ui.views.modeSelectView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.ui.views.util.viewModelFactory

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

    Column() {

        val generalModifier = Modifier
        if(modeSelectViewModel.isModeSelected(SessionType.GENERAL)){
            generalModifier.border(5.dp, Color.Black, RoundedCornerShape(50))
        }
        Button(onClick = { modeSelectViewModel.onSelectMode(SessionType.GENERAL) }, modifier = generalModifier){
            Text(text = "General Mode")
        }

        val artistModifier = Modifier
        if(modeSelectViewModel.isModeSelected(SessionType.ARTIST)){
            artistModifier.border(5.dp, Color.Black, RoundedCornerShape(50))
        }
        Button(onClick = { modeSelectViewModel.onSelectMode(SessionType.ARTIST) }, modifier = artistModifier){
            Text(text = "Artist Mode")
        }

        val genreModifier = Modifier
        if(modeSelectViewModel.isModeSelected(SessionType.GENRE)){
            genreModifier.border(5.dp, Color.Black, RoundedCornerShape(50))
        }
        Button(onClick = { modeSelectViewModel.onSelectMode(SessionType.GENRE) }, modifier = genreModifier){
            Text(text = "Genre Mode")
        }

        val playlistModifier = Modifier
        if(modeSelectViewModel.isModeSelected(SessionType.PLAYLIST)){
            playlistModifier.border(5.dp, Color.Black, RoundedCornerShape(50))
        }
        Button(onClick = { modeSelectViewModel.onSelectMode(SessionType.PLAYLIST) }, modifier = playlistModifier){
            Text(text = "Playlist Mode")
        }

        Text(text = modeSelectViewModel.getSelectedModeDescription())


        Button(onClick = { modeSelectViewModel.onConfirmMode(navController) }){
            Text(text = "Modus Bestätigen")
        }
    }
}