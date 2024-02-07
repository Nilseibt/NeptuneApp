package com.example.neptune.ui.views.loadingView

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.views.infoView.InfoViewModel
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun LoadingView(navController: NavController, activity: MainActivity, argument: String?) {

    val loadingViewModel = viewModel<LoadingViewModel>(
        factory = viewModelFactory {
            LoadingViewModel(
                NeptuneApp.model.appState,
                navController,
                activity,
                argument
            )
        }
    )

    BackHandler {
        loadingViewModel.onBack(navController)
    }

    Text(text = "LOADING... (Should take no more than 5 seconds)")

}