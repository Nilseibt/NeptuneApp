package com.example.neptune.ui.views.modeSelectView

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.ui.views.ViewsCollection

class ModeSelectViewModel(
    val appState: AppState
) : ViewModel() {

    private var selectedSessionType = mutableStateOf(SessionType.GENERAL)

    fun isModeSelected(mode: SessionType): Boolean {
        return selectedSessionType.value == mode
    }

    fun onSelectMode(mode: SessionType) {
        selectedSessionType.value = mode
    }

    fun getSelectedModeDescription(): String {
        return when (selectedSessionType.value) {
            SessionType.GENERAL -> "General Mode Beschreibung"
            SessionType.ARTIST -> "Artist Mode Beschreibung"
            SessionType.GENRE -> "Genre Mode Beschreibung"
            SessionType.PLAYLIST -> "Playlist Mode Beschreibung"
        }
    }

    fun onConfirmMode(navController: NavController) {
        appState.sessionBuilder.setSessionType(selectedSessionType.value)
        navController.navigate(ViewsCollection.MODE_SETTINGS_VIEW.name)
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }
}