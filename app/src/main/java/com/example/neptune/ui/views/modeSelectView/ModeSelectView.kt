package com.example.neptune.ui.views.modeSelectView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun ModeSelectView(navController: NavController) {

    val modeSelectViewModel = viewModel<ModeSelectViewModel>(
        factory = viewModelFactory {
            ModeSelectViewModel(
                //NeptuneApp.appState
            )
        }
    )

    BackHandler {
        modeSelectViewModel.onBack(navController)
    }
}