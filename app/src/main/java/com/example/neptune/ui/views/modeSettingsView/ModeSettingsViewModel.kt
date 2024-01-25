package com.example.neptune.ui.views.modeSettingsView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.data.model.session.SessionBuilder
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.ui.views.ViewsCollection
import kotlin.math.pow

class ModeSettingsViewModel(
    val appState: AppState
) : ViewModel() {

    private var playlistLinkInput by mutableStateOf("")

    private var sliderPosition by mutableFloatStateOf(0f)


    fun isPlaylistLinkInputAvailable(): Boolean {
        return getSessionType() == SessionType.PLAYLIST
    }

    fun getCurrentPlaylistLinkInput(): String {
        return playlistLinkInput
    }

    fun onPlaylistLinkInputChange(newInput: String) {
        playlistLinkInput = newInput
    }

    fun isPlaylistLinkValid(): Boolean {
        //TODO
        return true
    }

    fun getCooldownSliderPosition(): Float {
        return sliderPosition
    }

    fun onCooldownSliderPositionChange(newPosition: Float) {
        sliderPosition = newPosition
    }

    fun onCooldownSliderFinish() {

    }

    fun getTrackCooldownString(): String {
        return sliderPositionToCooldownMinutes(sliderPosition).toString() + " Minuten"
    }

    fun isArtistSession(): Boolean {
        return getSessionType() == SessionType.ARTIST
    }

    fun onArtistSearch(navController: NavController) {
        navController.navigate(ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.name)
    }

    fun isGenreSession(): Boolean {
        return getSessionType() == SessionType.GENRE
    }

    fun onGenreSearch(navController: NavController) {
        navController.navigate(ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.name)
    }

    fun getSelectedEntities(): List<String> {
        return appState.sessionBuilder.getSelectedEntities()
    }

    fun onToggleSelect(entityName: String) {
        if (appState.sessionBuilder.isEntitySelected(entityName)) {
            appState.sessionBuilder.removeEntity(entityName)
        } else {
            appState.sessionBuilder.addEntity(entityName)
        }
    }

    fun onConfirmSettings(navController: NavController) {
        if(getSessionType() == SessionType.PLAYLIST) {
            appState.sessionBuilder.setPlaylistLink(playlistLinkInput)
        }
        appState.sessionBuilder.setTrackCooldown(sliderPositionToCooldownMinutes(sliderPosition))
        //TODO clear the sessionbuilder
        navController.navigate(ViewsCollection.CONTROL_VIEW.name)
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }




    private fun getSessionType(): SessionType {
        return appState.sessionBuilder.getSessionType()
    }

    private fun sliderPositionToCooldownMinutes(sliderPosition: Float): Int {
        return (10f * (73f).pow(sliderPosition) - 10f).toInt()
    }

}