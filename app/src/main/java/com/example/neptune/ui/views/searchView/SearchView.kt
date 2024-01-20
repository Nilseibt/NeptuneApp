package com.example.neptune.ui.views.searchView

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun SearchView(navController: NavController) {

    val searchViewModel = viewModel<SearchViewModel>(
        factory = viewModelFactory {
            SearchViewModel(
                //NeptuneApp.???
            )
        }
    )

    BackHandler {
        searchViewModel.onBack(navController)
    }
}