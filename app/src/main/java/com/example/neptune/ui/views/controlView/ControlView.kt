package com.example.neptune.ui.views.controlView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun ControlView(navController: NavController) {

    val controlViewModel = viewModel<ControlViewModel>(
        factory = viewModelFactory {
            ControlViewModel(
                //NeptuneApp.???
            )
        }
    )

    BackHandler {
        controlViewModel.onBack(navController)
    }
}