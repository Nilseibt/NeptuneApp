package com.example.neptune.ui.views.modeSelectView

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp.Companion.context
import com.example.neptune.R
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
            SessionType.GENERAL -> context.getString(R.string.general_mode_description)
            SessionType.ARTIST -> context.getString(R.string.artist_mode_description)
            SessionType.GENRE -> context.getString(R.string.genre_mode_description)
            SessionType.PLAYLIST -> context.getString(R.string.playlist_mode_description)
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