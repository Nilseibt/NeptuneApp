package com.example.neptune.ui.views.infoView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun InfoView(navController: NavController) {

    val infoViewModel = viewModel<InfoViewModel>(
        factory = viewModelFactory {
            InfoViewModel(
                //NeptuneApp.appState
            )
        }
    )

    BackHandler {
        infoViewModel.onBack(navController)
    }
}