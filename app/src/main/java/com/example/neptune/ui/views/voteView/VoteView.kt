package com.example.neptune.ui.views.voteView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun VoteView(navController: NavController) {

    val voteViewModel = viewModel<VoteViewModel>(
        factory = viewModelFactory {
            VoteViewModel(
                //NeptuneApp.???
            )
        }
    )

    BackHandler {
        voteViewModel.onBack(navController)
    }
}