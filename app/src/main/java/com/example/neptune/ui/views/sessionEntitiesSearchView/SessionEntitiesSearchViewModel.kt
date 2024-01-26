package com.example.neptune.ui.views.sessionEntitiesSearchView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.session.SessionType

class SessionEntitiesSearchViewModel(
    val appState: AppState
) : ViewModel() {

    private var entitySearchInput by mutableStateOf("")

    private var entitiesSearchList = mutableStateListOf<String>()

    fun getSearchInput(): String {
        return entitySearchInput
    }

    fun onSearchInputChange(newInput: String) {
        entitySearchInput = newInput
        if (getSessionType() == SessionType.ARTIST) {
            appState.streamingEstablisher.searchMatchingArtists(entitySearchInput) {
                artistSearchCallback(it)
            }
        } else if (getSessionType() == SessionType.GENRE) {
            entitiesSearchList =
                appState.sessionBuilder.searchMatchingGenres(entitySearchInput).toMutableStateList()
        }
    }

    fun onToggleSelect(entityName: String) {
        if (appState.sessionBuilder.isEntitySelected(entityName)) {
            appState.sessionBuilder.removeEntity(entityName)
        } else {
            appState.sessionBuilder.addEntity(entityName)
        }
    }

    fun getEntitiesSearchList(): List<String> {
        return entitiesSearchList
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }


    private fun artistSearchCallback(artistList: List<String>) {
        entitiesSearchList = artistList.toMutableStateList()
    }


    private fun getSessionType(): SessionType {
        return appState.sessionBuilder.getSessionType()
    }

}