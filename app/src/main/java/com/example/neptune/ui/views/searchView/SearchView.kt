package com.example.neptune.ui.views.searchView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.commons.TrackListComposable
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun SearchView(navController: NavController) {

    val searchViewModel = viewModel<SearchViewModel>(
        factory = viewModelFactory {
            SearchViewModel(
                NeptuneApp.model.user!!
            )
        }
    )

    BackHandler {
        searchViewModel.onBack(navController)
    }

    Column(Modifier.padding(10.dp)) {

        Button(onClick = { searchViewModel.onOpenInfo(navController) }) {
            Text("Info öffnen (Icon)")
        }

        Text("TopBar Beschr.: " + searchViewModel.getTopBarDescription())

        Button(onClick = { searchViewModel.onOpenStats(navController) }) {
            Text("Statistiken öffnen (Icon)")
        }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchViewModel.getTrackSearchInput(),
            onValueChange = { searchViewModel.onTrackSearchInputChange(it) })
        Box {
            TrackListComposable(
                tracks = searchViewModel.getSearchList(),
                trackListType = searchViewModel.getSearchTrackListType(),
                onToggleUpvote = { searchViewModel.onToggleUpvote(it) },
                onToggleDropdown = { searchViewModel.onToggleDropdown(it) },
                isDropdownExpanded = { searchViewModel.isDropdownExpanded(it) },

                onToggleBlock = { searchViewModel.onToggleBlock(it) })
        }
    }
}