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

/**
 * ViewModel class for controlling the logic of the ModeSelectView.
 *
 * @property appState The current app state.
 */
class ModeSelectViewModel(
    val appState: AppState
) : ViewModel() {

    private var selectedSessionType = mutableStateOf(SessionType.GENERAL)

    /**
     * Checks if the given mode is currently selected.
     *
     * @param mode The session type to check.
     * @return True if the mode is currently selected, false otherwise.
     */
    fun isModeSelected(mode: SessionType): Boolean {
        return selectedSessionType.value == mode
    }

    /**
     * Selects the given mode.
     *
     * @param mode The session type to select.
     */
    fun onSelectMode(mode: SessionType) {
        selectedSessionType.value = mode
    }

    /**
     * Retrieves the description of the selected mode.
     *
     * @return The description of the selected mode.
     */
    fun getSelectedModeDescription(): String {
        return when (selectedSessionType.value) {
            SessionType.GENERAL -> context.getString(R.string.general_mode_description)
            SessionType.ARTIST -> context.getString(R.string.artist_mode_description)
            SessionType.GENRE -> context.getString(R.string.genre_mode_description)
            SessionType.PLAYLIST -> context.getString(R.string.playlist_mode_description)
        }
    }

    /**
     * Handles the confirmation of the selected mode.
     * This method sets the selected session type in the app state and navigates to the mode settings view.
     *
     * @param navController The navigation controller used for navigation.
     */
    fun onConfirmMode(navController: NavController) {
        appState.sessionBuilder.setSessionType(selectedSessionType.value)
        navController.navigate(ViewsCollection.MODE_SETTINGS_VIEW.name)
    }

    /**
     * Handles the back action.
     * This method pops the back stack to navigate back to the previous destination.
     *
     * @param navController The navigation controller used for navigation.
     */
    fun onBack(navController: NavController) {
        navController.popBackStack()
    }
}