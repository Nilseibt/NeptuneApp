package com.example.neptune.ui.views.searchView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.commons.SessionInfoBar
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.commons.TrackListComposable
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
import kotlinx.coroutines.delay

/**
 * The composable for the searchView.
 *
 * @param navController The NavController needed to navigate to another view.
 */
@Composable
fun SearchView(navController: NavController) {

    val searchViewModel = viewModel<SearchViewModel>(
        factory = viewModelFactory {
            SearchViewModel(
                NeptuneApp.model.appState.user!!
            )
        }
    )

    BackHandler {
        searchViewModel.onBack(navController)
    }

    // Synchronizes the view with the search entry.
    LaunchedEffect(
        key1 = Unit,
        block = {
            while (true) {
                searchViewModel.checkToUpdateSearch()
                delay(100)
            }
        }
    )

    // Synchronizes the view with the server.
    LaunchedEffect(
        key1 = Unit,
        block = {
            while (true) {
                searchViewModel.syncState()
                delay(5000)
            }
        }
    )

    NeptuneTheme {

        SearchViewContent(searchViewModel, navController)

    }

}

@Composable
private fun SearchViewContent(searchViewModel: SearchViewModel, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        TopBar(onBack = { searchViewModel.onBack(navController) })

        SessionInfoBar(
            onStatistics = { searchViewModel.onOpenStats(navController) },
            onInfo = { searchViewModel.onOpenInfo(navController) },
            description = searchViewModel.getTopBarDescription()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            Box(modifier = Modifier.weight(1f)) {
                SearchBar(searchViewModel)
            }

            Box(modifier = Modifier.weight(8f)) {

                val tracks = when (searchViewModel.getActiveFilter()) {
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
                    onToggleBlock = { searchViewModel.onToggleBlock(it) }
                )

            }

        }

    }

}

@Composable
private fun SearchBar(searchViewModel: SearchViewModel) {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (searchViewModel.isHost()) {
            Box(modifier = Modifier.weight(1f)) {
                FilterButton(searchViewModel)
            }
        }

        Box(modifier = Modifier.weight(7f)) {
            SearchField(searchViewModel)
        }

    }

}

@Composable
private fun FilterButton(searchViewModel: SearchViewModel) {

    IconButton(
        onClick = { searchViewModel.onClickFilterIcon() },
        modifier = Modifier.fillMaxSize()
    ) {

        val icon = when (searchViewModel.getActiveFilter()) {
            Filter.NONE -> painterResource(id = R.drawable.baseline_filter_alt_24)
            Filter.BLOCKED -> painterResource(id = R.drawable.baseline_block_24)
            Filter.COOLDOWN -> painterResource(id = R.drawable.baseline_lock_clock_24)
        }

        Icon(
            painter = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        )

    }

    DropdownMenu(
        expanded = searchViewModel.isFilterDropdownExpanded(),
        onDismissRequest = { searchViewModel.collapseFilterDropwdown() }
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.locked_filter_name)) },
            onClick = { searchViewModel.onSetActiveFilter(Filter.BLOCKED) })
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.cooldown_filter_name)) },
            onClick = { searchViewModel.onSetActiveFilter(Filter.COOLDOWN) })
    }

}

@Composable
private fun SearchField(searchViewModel: SearchViewModel) {

    OutlinedTextField(
        value = searchViewModel.getTrackSearchInput(),
        onValueChange = { searchViewModel.onTrackSearchInputChange(it) },
        enabled = searchViewModel.getActiveFilter() == Filter.NONE,
        label = { Text(text = stringResource(id = R.string.search_text)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
            focusedLabelColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier.fillMaxWidth()
    )

}
