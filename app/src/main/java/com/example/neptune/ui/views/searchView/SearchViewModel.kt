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

/**
 * ViewModel class for controlling the logic of the SearchView.
 *
 * @property user The user of the current session.
 */
class SearchViewModel(
    val user: User
) : ViewModel() {

    private var searchInput by mutableStateOf("")

    private var expandedDropdownIndex by mutableIntStateOf(-1)

    private var activeFilter = mutableStateOf(Filter.NONE)
    private var isFilterDropdownExpanded = mutableStateOf(false)

    private var inputChanged = false
    private var lastInputChangeTimestamp = 0L

    /**
     * Retrieves the type of track list to be searched based on the user's role.
     *
     * @return The type of track list to be searched.
     */
    fun getSearchTrackListType(): TrackListType {
        if (user is Host) {
            return TrackListType.HOST_SEARCH
        } else {
            return TrackListType.PARTICIPANT_SEARCH
        }
    }

    /**
     * Retrieves the current search input.
     *
     * @return The current search input.
     */
    fun getTrackSearchInput(): String {
        return searchInput
    }

    /**
     * Handles changes in the search input.
     * If the search input is empty, clears the search list.
     *
     * @param newInput The new search input.
     */
    fun onTrackSearchInputChange(newInput: String) {
        searchInput = newInput
        inputChanged = true
        lastInputChangeTimestamp = System.currentTimeMillis()
        if (searchInput == "") {
            user.searchList.value.clear()
        }
    }

    /**
     * Checks if there has been a change in the search input and updates the search if needed.
     * The search is updated if the input has changed and the elapsed time since the last input change is greater than 500 milliseconds.
     */
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

    /**
     * Toggles the upvote status of a track.
     *
     * @param track The track to toggle the upvote for.
     */
    fun onToggleUpvote(track: Track) {
        user.toggleUpvote(track)
    }

    /**
     * Toggles the dropdown for a specific track in the list.
     *
     * @param index The index of the track in the list.
     */
    fun onToggleDropdown(index: Int) {
        if (expandedDropdownIndex == -1) {
            expandedDropdownIndex = index
        } else {
            expandedDropdownIndex = -1
        }
    }

    /**
     * Checks if the dropdown for a specific track in the list is expanded.
     *
     * @param index The index of the track in the list.
     * @return `true` if the dropdown is expanded, `false` otherwise.
     */
    fun isDropdownExpanded(index: Int): Boolean {
        return expandedDropdownIndex == index
    }

    /**
     * Adds a track to the queue.
     *
     * @param track The track to add to the queue.
     */
    fun onAddToQueue(track: Track) {
        expandedDropdownIndex = -1
        (user as Host).addTrackToQueue(track)
    }

    /**
     * Toggles the block status of a track.
     *
     * @param track The track to toggle the block status for.
     */
    fun onToggleBlock(track: Track) {
        expandedDropdownIndex = -1
        (user as Host).toggleBlockTrack(track)
    }

    /**
     * Retrieves the list of tracks resulting from the search operation.
     *
     * @return The list of tracks resulting from the search.
     */
    fun getSearchList(): SnapshotStateList<MutableState<Track>> {
        return user.searchList.value.getTracks()
    }

    /**
     * Checks if the user is a host.
     *
     * @return `true` if the user is a host, `false` otherwise.
     */
    fun isHost(): Boolean {
        return user is Host
    }

    /**
     * Handles the click event of the filter icon.
     * Toggles the filter dropdown if it's closed, otherwise resets the active filter.
     */
    fun onClickFilterIcon() {
        if (activeFilter.value == Filter.NONE) {
            isFilterDropdownExpanded.value = true
        } else {
            activeFilter.value = Filter.NONE
        }
    }

    /**
     * Collapses the filter dropdown.
     */
    fun collapseFilterDropwdown() {
        isFilterDropdownExpanded.value = false
    }

    /**
     * Checks if the filter dropdown is expanded.
     *
     * @return `true` if the filter dropdown is expanded, `false` otherwise.
     */
    fun isFilterDropdownExpanded(): Boolean {
        return isFilterDropdownExpanded.value
    }

    /**
     * Retrieves the active filter.
     *
     * @return The active filter.
     */
    fun getActiveFilter(): Filter {
        return activeFilter.value
    }

    /**
     * Sets the active filter and collapses the filter dropdown.
     *
     * @param filter The filter to set as active.
     */
    fun onSetActiveFilter(filter: Filter) {
        activeFilter.value = filter
        collapseFilterDropwdown()
    }

    /**
     * Retrieves the list of tracks in cooldown.
     *
     * @return The list of tracks in cooldown.
     */
    fun getCooldownTracks(): SnapshotStateList<MutableState<Track>> {
        return user.cooldownList.value.getTracks()
    }

    /**
     * Retrieves the list of blocked tracks.
     *
     * @return The list of blocked tracks.
     */
    fun getBlockedTracks(): SnapshotStateList<MutableState<Track>> {
        return user.blockList.value.getTracks()
    }

    /**
     * Retrieves the session type displayed in the top bar.
     *
     * @return The session type displayed in the top bar.
     */
    fun getTopBarDescription(): SessionType {
        return user.session.sessionType
    }

    /**
     * Navigates to the information view.
     *
     * @param navController The NavController to use for navigation.
     */
    fun onOpenInfo(navController: NavController) {
        navController.navigate(ViewsCollection.INFO_VIEW.name)
    }

    /**
     * Navigates to the stats view.
     *
     * @param navController The NavController to use for navigation.
     */
    fun onOpenStats(navController: NavController) {
        navController.navigate(ViewsCollection.STATS_VIEW.name)
    }

    /**
     * Handles the back navigation event.
     * Clears the search list and pops the back stack.
     *
     * @param navController The NavController to use for navigation.
     */
    fun onBack(navController: NavController) {
        user.searchList.value.clear()
        navController.popBackStack()
    }

    /**
     * Synchronizes the users state with the backend and the host state with the streaming service.
     */
    fun syncState() {
        user.syncState()
    }

}


/**
 * Enum class representing different types of filters.
 * This enum class defines the available filters for tracks.
 */
enum class Filter {
    NONE, BLOCKED, COOLDOWN
}