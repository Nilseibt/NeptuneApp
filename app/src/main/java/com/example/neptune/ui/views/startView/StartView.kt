package com.example.neptune.ui.views.startView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun StartView(navController: NavController) {

    val startViewModel = viewModel<StartViewModel>(
        factory = viewModelFactory {
            StartViewModel(
                //NeptuneApp.appState
            )
        }
    )

    BackHandler {
        startViewModel.onBack(navController)
    }
}







