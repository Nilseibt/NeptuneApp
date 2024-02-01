package com.example.neptune.ui.views.searchView

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.track.src.Track
import com.example.neptune.data.model.user.src.Host
import com.example.neptune.data.model.user.src.User
import com.example.neptune.ui.commons.TrackListType
import com.example.neptune.ui.views.ViewsCollection

class SearchViewModel(
    val user: User
) : ViewModel() {

    private var searchInput by mutableStateOf("")

    private var expandedDropdownIndex by mutableIntStateOf(-1)

    private var activeFilter = mutableStateOf(Filter.NONE)
    private var isFilterDropdownExpanded = mutableStateOf(false)

    fun getSearchTrackListType(): TrackListType {
        if (user is Host) {
            return TrackListType.HOST_SEARCH
        } else {
            return TrackListType.PARTICIPANT_SEARCH
        }
    }

    fun getTrackSearchInput(): String {
        return searchInput
    }

    fun onTrackSearchInputChange(newInput: String) {
        searchInput = newInput
        if (user.session.sessionType != SessionType.GENRE) {
            if (searchInput != "") {
                user.search(searchInput)
            }
        }
    }

    fun isSearchButtonActive(): Boolean {
        //TODO
        return false
    }

    fun onSearchButtonClick() {
        //TODO
    }

    fun onToggleUpvote(track: Track) {
        user.toggleUpvote(track)
    }

    fun onToggleDropdown(index: Int) {
        if (expandedDropdownIndex == -1) {
            expandedDropdownIndex = index
        } else {
            expandedDropdownIndex = -1
        }
    }

    fun isDropdownExpanded(index: Int): Boolean {
        return expandedDropdownIndex == index
    }

    fun onAddToQueue(track: Track) {
        (user as Host).addTrackToQueue(track)
    }

    fun onToggleBlock(track: Track) {
        (user as Host).toggleBlockTrack(track)
    }

    fun getSearchList(): SnapshotStateList<MutableState<Track>> {
        return user.searchList.value.getTracks()
    }

    fun isHost(): Boolean {
        return user is Host
    }

    fun onClickFilterIcon() {
        if (activeFilter.value == Filter.NONE) {
            isFilterDropdownExpanded.value = true
        } else {
            activeFilter.value = Filter.NONE
        }
    }

    fun collapseFilterDropwdown() {
        isFilterDropdownExpanded.value = false
    }

    fun isFilterDropdownExpanded(): Boolean{
        return isFilterDropdownExpanded.value
    }

    fun getActiveFilter(): Filter {
        return activeFilter.value
    }

    fun onSetActiveFilter(filter: Filter) {
        activeFilter.value = filter
        collapseFilterDropwdown()
    }

    fun getCooldownTracks(): SnapshotStateList<MutableState<Track>> {
        return user.cooldownList.value.getTracks()
    }

    fun getBlockedTracks(): SnapshotStateList<MutableState<Track>> {
        return user.blockList.value.getTracks()
    }

    fun getTopBarDescription(): String {
        return when (user.session.sessionType) {
            SessionType.GENERAL -> "General Mode"
            SessionType.ARTIST -> "Artist Mode"
            SessionType.GENRE -> "Genre Mode"
            SessionType.PLAYLIST -> "Playlist Mode"
        }
    }

    fun onOpenInfo(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }


    fun onOpenStats(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }


    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}


enum class Filter {
    NONE, BLOCKED, COOLDOWN
}