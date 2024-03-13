package com.example.neptune.ui.views.sessionEntitiesSearchView

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.session.SessionType


/**
 * ViewModel class for controlling the logic of the SessionEntitiesSearchView.
 *
 * @property appState The current app state.
 */
class SessionEntitiesSearchViewModel(
    val appState: AppState
) : ViewModel() {

    private var entitySearchInput by mutableStateOf("")

    private var entitiesSearchList by mutableStateOf(mutableStateListOf<String>())


    /**
     * Retrieves the current search input string.
     *
     * @return The current search input string.
     */
    fun getSearchInput(): String {
        return entitySearchInput
    }

    /**
     * Updates the search input string and performs entity search based on the session type.
     *
     * @param newInput The new search input string.
     */
    fun onSearchInputChange(newInput: String) {
        entitySearchInput = newInput
        if (entitySearchInput != "") {
            if (getSessionType() == SessionType.ARTIST) {
                appState.streamingEstablisher.searchMatchingArtists(entitySearchInput) {
                    artistSearchCallback(it)
                }
            } else if (getSessionType() == SessionType.GENRE) {
                entitiesSearchList =
                    appState.sessionBuilder.searchMatchingGenres(entitySearchInput)
                        .toMutableStateList()
            }
        }
    }

    /**
     * Toggles the selection state of an entity.
     *
     * @param entityName The name of the entity to toggle.
     */
    fun onToggleSelect(entityName: String) {
        if (appState.sessionBuilder.isEntitySelected(entityName)) {
            appState.sessionBuilder.removeEntity(entityName)
        } else {
            appState.sessionBuilder.addEntity(entityName)
        }
    }

    /**
     * Retrieves the list of search results for session entities.
     *
     * @return The list of search results for session entities.
     */
    fun getEntitiesList(): List<String> {
        return entitiesSearchList
    }

    /**
     * Handles the back navigation action.
     *
     * @param navController The NavController instance.
     */
    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

    /**
     * Checks if an entity is selected.
     *
     * @param entityName The name of the entity.
     * @return True if the entity is selected, false otherwise.
     */
    fun isEntitySelected(entityName: String): Boolean {
        return appState.sessionBuilder.isEntitySelected(entityName)
    }

    /**
     * Retrieves the description for the current search mode (artist or genre).
     *
     * @return The description for the current search mode.
     */
    fun getSearchDescription(): String {
        return if (getSessionType() == SessionType.ARTIST) {
            NeptuneApp.context.getString(R.string.artist_search)
        } else {
            NeptuneApp.context.getString(R.string.genre_search)
        }
    }


    private fun artistSearchCallback(artistList: List<String>) {
        entitiesSearchList = artistList.toMutableStateList()
    }


    private fun getSessionType(): SessionType {
        return appState.sessionBuilder.getSessionType()
    }

}