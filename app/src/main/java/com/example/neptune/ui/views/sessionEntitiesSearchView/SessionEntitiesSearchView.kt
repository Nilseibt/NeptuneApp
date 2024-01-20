package com.example.neptune.ui.views.sessionEntitiesSearchView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.statsView.StatsViewModel
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun SessionEntitiesSearchView(navController: NavController) {

    val sessionEntitiesSearchViewModel = viewModel<SessionEntitiesSearchViewModel>(
        factory = viewModelFactory {
            SessionEntitiesSearchViewModel(
                //NeptuneApp.???
            )
        }
    )

    BackHandler {
        sessionEntitiesSearchViewModel.onBack(navController)
    }
}