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
import com.example.neptune.data.model.track.Track
import com.example.neptune.data.model.user.Host
import com.example.neptune.data.model.user.User
import com.example.neptune.ui.commons.TrackListType
import com.example.neptune.ui.views.ViewsCollection

class SearchViewModel(
    val user: User
) : ViewModel() {

    private var searchInput by mutableStateOf("")

    private var expandedDropdownIndex by mutableIntStateOf(-1)

    private var activeFilter = mutableStateOf(Filter.NONE)
    private var isFilterDropdownExpanded = mutableStateOf(false)

    private var inputChanged = false
    private var lastInputChangeTimestamp = 0L

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
        inputChanged = true
        lastInputChangeTimestamp = System.currentTimeMillis()
        if (searchInput == "") {
            user.searchList.value.clear()
        }
    }

    fun checkToUpdateSearch() {
        val currentTimestamp = System.currentTimeMillis()
        if (inputChanged && currentTimestamp - lastInputChangeTimestamp > 500) {
            inputChanged = false
            if (searchInput != "") {
                user.search(searchInput)
            } else {
                user.searchList.value.clear()
            }
        }
    }

    fun isSearchButtonActive(): Boolean {
        return user.session.sessionType == SessionType.GENRE
    }

    fun onSearchButtonClick() {
        if (searchInput != "") {
            user.search(searchInput)
        }
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
        expandedDropdownIndex = -1
        (user as Host).addTrackToQueue(track)
    }

    fun onToggleBlock(track: Track) {
        expandedDropdownIndex = -1
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

    fun isFilterDropdownExpanded(): Boolean {
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

    fun getTopBarDescription(): SessionType {
        return user.session.sessionType
    }

    fun onOpenInfo(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }


    fun onOpenStats(navController: NavController) {
        navController.navigate(ViewsCollection.STATS_VIEW.name)
    }


    fun onBack(navController: NavController) {
        user.searchList.value.clear()
        navController.popBackStack()
    }

    fun syncState() {
        user.syncState()
    }

}


enum class Filter {
    NONE, BLOCKED, COOLDOWN
}