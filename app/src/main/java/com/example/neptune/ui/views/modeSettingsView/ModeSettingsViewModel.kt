package com.example.neptune.ui.views.modeSettingsView

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.track.Track
import com.example.neptune.ui.views.ViewsCollection
import kotlin.math.pow


/**
 * ViewModel class for controlling the logic of the ModeSettingsView.
 *
 * @property appState The current app state.
 */
class ModeSettingsViewModel(
    val appState: AppState
) : ViewModel() {

    private var playlistLinkInput by mutableStateOf("")

    private var sliderPosition by mutableFloatStateOf(0f)

    private var isPlaylistLinkAccepted by mutableStateOf(false)

    private var playlistTracks = mutableListOf<Track>()


    /**
     * Checks if the playlist link input field should be available based on the current session type.
     *
     * @return True if the playlist link input field should be available, false otherwise.
     */
    fun isPlaylistLinkInputAvailable(): Boolean {
        return getSessionType() == SessionType.PLAYLIST
    }

    /**
     * Retrieves the current playlist link input.
     *
     * @return The current playlist link input.
     */
    fun getCurrentPlaylistLinkInput(): String {
        return playlistLinkInput
    }

    /**
     * Handles changes to the playlist link input.
     * Checks the format and the actual content of the input for validity.
     *
     * @param newInput The new playlist link input.
     */
    fun onPlaylistLinkInputChange(newInput: String) {
        playlistLinkInput = newInput
        if (playlistLinkInput == "") {
            isPlaylistLinkAccepted = false
        } else {
            val playlistLinkPattern = Regex("""playlist\/(.*?)\?si=.*""")
            val playlistLinkMatchResult = playlistLinkPattern.find(playlistLinkInput)
            Log.i("RES", (playlistLinkMatchResult != null).toString())
            if (playlistLinkMatchResult != null) {
                appState.streamingEstablisher.getPlaylist(playlistLinkMatchResult.groupValues[1]) { resultList ->
                    playlistTracks = resultList
                    isPlaylistLinkAccepted = true
                }
            } else {
                isPlaylistLinkAccepted = false
            }
        }
    }

    /**
     * Checks if the playlist link is valid.
     *
     * @return True if the playlist link is valid, false otherwise.
     */
    fun isPlaylistLinkValid(): Boolean {
        return isPlaylistLinkAccepted
    }

    /**
     * Retrieves the position of the cooldown slider.
     *
     * @return The position of the cooldown slider from 0 to 1.
     */
    fun getCooldownSliderPosition(): Float {
        return sliderPosition
    }

    /**
     * Handles changes to the cooldown slider position.
     *
     * @param newPosition The new position of the cooldown slider from 0 to 1.
     */
    fun onCooldownSliderPositionChange(newPosition: Float) {
        sliderPosition = newPosition
    }

    /**
     * Retrieves the string representation of the track cooldown.
     *
     * @return The string representation of the track cooldown.
     */
    fun getTrackCooldownString(): String {
        return sliderPositionToCooldownMinutes(sliderPosition).toString() + " Minuten" //TODO make string resource
    }

    /**
     * Checks if the current session is an artist session.
     *
     * @return True if the current session is an artist session, false otherwise.
     */
    fun isArtistSession(): Boolean {
        return getSessionType() == SessionType.ARTIST
    }

    /**
     * Navigates to the artist search view.
     *
     * @param navController The navigation controller used for navigation.
     */
    fun onArtistSearch(navController: NavController) {
        navController.navigate(ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.name)
    }

    /**
     * Checks if the current session is a genre session.
     *
     * @return True if the current session is a genre session, false otherwise.
     */
    fun isGenreSession(): Boolean {
        return getSessionType() == SessionType.GENRE
    }

    /**
     * Navigates to the genre search view.
     *
     * @param navController The navigation controller used for navigation.
     */
    fun onGenreSearch(navController: NavController) {
        navController.navigate(ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.name)
    }

    /**
     * Checks if the current session is a playlist session.
     *
     * @return True if the current session is a playlist session, false otherwise.
     */
    fun isPlaylistSession(): Boolean {
        return getSessionType() == SessionType.PLAYLIST
    }

    /**
     * Retrieves the selected entities for the session.
     *
     * @return The list of selected entities.
     */
    fun getSelectedEntities(): List<String> {
        return appState.sessionBuilder.getSelectedEntities()
    }

    /**
     * Toggles the selection of the given entity.
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
     * Handles confirmation of the session settings.
     * This method sets the playlist tracks and track cooldown in the session builder and creates a new session.
     *
     * @param navController The navigation controller used for navigation.
     */
    fun onConfirmSettings(navController: NavController) {
        if (getSessionType() == SessionType.PLAYLIST) {
            appState.sessionBuilder.setPlaylistTracks(playlistTracks)
        }
        appState.sessionBuilder.setTrackCooldown(sliderPositionToCooldownMinutes(sliderPosition))
        NeptuneApp.model.appState.createNewSessionAndJoin(navController)
    }

    /**
     * Handles the back action.
     * This method resets the session builder and pops the back stack to navigate back to the previous destination.
     *
     * @param navController The navigation controller used for navigation.
     */
    fun onBack(navController: NavController) {
        appState.sessionBuilder.reset()
        navController.popBackStack()
    }


    private fun getSessionType(): SessionType {
        return appState.sessionBuilder.getSessionType()
    }

    private fun sliderPositionToCooldownMinutes(sliderPosition: Float): Int {
        return (10f * (73f).pow(sliderPosition) - 10f).toInt()
    }

}