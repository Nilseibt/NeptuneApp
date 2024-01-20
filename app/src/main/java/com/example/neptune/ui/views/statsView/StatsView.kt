package com.example.neptune.ui.views.statsView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory
import com.example.neptune.ui.views.voteView.VoteViewModel

@Composable
fun StatsView(navController: NavController) {

    val statsViewModel = viewModel<StatsViewModel>(
        factory = viewModelFactory {
            StatsViewModel(
                //NeptuneApp.???
            )
        }
    )

    BackHandler {
        statsViewModel.onBack(navController)
    }
}