package com.example.neptune.ui.views.modeSelectView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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

    Column() {
        Button(onClick = { modeSelectViewModel.onConfirmMode(navController) }){
            Text(text = "Modus Bestätigen")
        }
    }
}