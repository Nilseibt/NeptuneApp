package com.example.neptune.ui.views.searchView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.commons.TrackListComposable
import com.example.neptune.ui.commons.TrackListType
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

        if (searchViewModel.isHost()) {
            Box {
                IconButton(onClick = { searchViewModel.onClickFilterIcon() }) {
                    val icon = when (searchViewModel.getActiveFilter()) {
                        Filter.NONE -> Icons.Default.ArrowDropDown
                        Filter.BLOCKED -> Icons.Default.Lock
                        Filter.COOLDOWN -> Icons.Default.DateRange
                    }
                    Icon(icon, "", tint = Color.Black)
                }
                DropdownMenu(
                    expanded = searchViewModel.isFilterDropdownExpanded(),
                    onDismissRequest = { searchViewModel.collapseFilterDropwdown() }) {
                    DropdownMenuItem(
                        text = { Text("Gesperrt") },
                        onClick = { searchViewModel.onSetActiveFilter(Filter.BLOCKED) })
                    DropdownMenuItem(
                        text = { Text("Cooldown") },
                        onClick = { searchViewModel.onSetActiveFilter(Filter.COOLDOWN) })
                }
            }
        }

        if(searchViewModel.getActiveFilter() == Filter.NONE) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchViewModel.getTrackSearchInput(),
                onValueChange = { searchViewModel.onTrackSearchInputChange(it) })
        }
        if(searchViewModel.isSearchButtonActive()){
            Button(onClick = { searchViewModel.onSearchButtonClick() }) {
                Text(text = "Suchen")
            }
        }
        Box {
            val tracks = when(searchViewModel.getActiveFilter()){
                Filter.NONE -> searchViewModel.getSearchList()
                Filter.BLOCKED -> searchViewModel.getBlockedTracks()
                Filter.COOLDOWN -> searchViewModel.getCooldownTracks()
            }
            TrackListComposable(
                tracks = tracks,
                trackListType = searchViewModel.getSearchTrackListType(),
                onToggleUpvote = { searchViewModel.onToggleUpvote(it) },
                onToggleDropdown = { searchViewModel.onToggleDropdown(it) },
                isDropdownExpanded = { searchViewModel.isDropdownExpanded(it) },
                onAddToQueue = { searchViewModel.onAddToQueue(it) },
                onToggleBlock = { searchViewModel.onToggleBlock(it) })
        }
    }
}