package com.example.neptune.ui.views.sessionEntitiesSearchView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.views.statsView.StatsViewModel
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun SessionEntitiesSearchView(navController: NavController) {

    val sessionEntitiesSearchViewModel = viewModel<SessionEntitiesSearchViewModel>(
        factory = viewModelFactory {
            SessionEntitiesSearchViewModel(
                NeptuneApp.model.appState
            )
        }
    )

    BackHandler {
        sessionEntitiesSearchViewModel.onBack(navController)
    }

    Column {
        TextField(
            value = sessionEntitiesSearchViewModel.getSearchInput(),
            onValueChange = { sessionEntitiesSearchViewModel.onSearchInputChange(it) })

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            sessionEntitiesSearchViewModel.getEntitiesSearchList().forEach {
                Button(onClick = { sessionEntitiesSearchViewModel.onToggleSelect(it) }) {
                    Text(text = it)
                }
            }
        }
    }
}