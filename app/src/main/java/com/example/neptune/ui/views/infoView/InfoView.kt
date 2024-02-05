package com.example.neptune.ui.views.infoView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun InfoView(navController: NavController) {

    val infoViewModel = viewModel<InfoViewModel>(
        factory = viewModelFactory {
            InfoViewModel(
                NeptuneApp.model.user!!
            )
        }
    )

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {

        TopBar(onBack = { infoViewModel.onBack(navController) })

        Text(text = "Session Code: " + infoViewModel.getSessionCode())

        Text(text = infoViewModel.getModeName())

        if(infoViewModel.isArtistMode()){
            Text(text = "Artists der Session:")
            infoViewModel.getAllArtists().forEach {
                Text(text = it)
            }
        }

        if(infoViewModel.isGenreMode()){
            Text(text = "Genres der Session:")
            infoViewModel.getAllGenres().forEach {
                Text(text = it)
            }
        }

        Text(text = "Hier vermutlich kein Beitrittscode, denn der geht mir am Arsch vorbei")

        Text(text = infoViewModel.getShareLink())
    }



    BackHandler {
        infoViewModel.onBack(navController)
    }
}

@Preview
@Composable
fun InfoPreview() {

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {

        TopBar {}

    }

}

/*
Column {

        Button(onClick = { infoViewModel.onBack(navController) }) {
            Text(text = "Info schließen (Icon)")
        }

        Text(text = "Session Infos (TopBar Beschriftung)")

        Button(onClick = { infoViewModel.onOpenStats(navController) }) {
            Text(text = "Statistiken öffnen (Icon)")
        }

        Text(text = "Session Code: " + infoViewModel.getSessionCode())

        Text(text = infoViewModel.getModeName())

        if(infoViewModel.isArtistMode()){
            Text(text = "Artists der Session:")
            infoViewModel.getAllArtists().forEach {
                Text(text = it)
            }
        }

        if(infoViewModel.isGenreMode()){
            Text(text = "Genres der Session:")
            infoViewModel.getAllGenres().forEach {
                Text(text = it)
            }
        }

        Text(text = "Hier vermutlich kein Beitrittscode, denn der geht mir am Arsch vorbei")

        Text(text = infoViewModel.getShareLink())
    }
 */