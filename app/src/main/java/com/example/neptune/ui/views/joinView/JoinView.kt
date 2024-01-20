package com.example.neptune.ui.views.joinView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun JoinView(navController: NavController) {

    val joinViewModel = viewModel<JoinViewModel>(
        factory = viewModelFactory {
            JoinViewModel(
                //NeptuneApp.appState
            )
        }
    )

    BackHandler {
        joinViewModel.onBack(navController)
    }
}