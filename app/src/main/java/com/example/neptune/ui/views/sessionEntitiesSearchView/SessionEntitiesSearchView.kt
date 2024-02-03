package com.example.neptune.ui.views.sessionEntitiesSearchView

import android.graphics.drawable.Icon
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.statsView.StatsViewModel
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun SessionEntitiesSearchView(navController: NavController) {

    NeptuneTheme {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background) {
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

                        var isSelected by remember { mutableStateOf(false) }
                        isSelected = sessionEntitiesSearchViewModel.isEntitySelected(it)
                        Button(onClick = {
                            //isSelected = !isSelected
                            sessionEntitiesSearchViewModel.onToggleSelect(it)
                        }) {
                            Text(text = it)
                            //Text(text = if (isSelected) "Selected: $it" else it)
                            Icon(imageVector = if (isSelected) Icons.Default.Clear else Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }
        }
    }


}