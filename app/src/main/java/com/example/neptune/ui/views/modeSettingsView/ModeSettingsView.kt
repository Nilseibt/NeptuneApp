package com.example.neptune.ui.views.modeSettingsView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun ModeSettingsView(navController: NavController) {

    val modeSettingsViewModel = viewModel<ModeSettingsViewModel>(
        factory = viewModelFactory {
            ModeSettingsViewModel(
                //NeptuneApp.appState
            )
        }
    )

    BackHandler {
        modeSettingsViewModel.onBack(navController)
    }
}