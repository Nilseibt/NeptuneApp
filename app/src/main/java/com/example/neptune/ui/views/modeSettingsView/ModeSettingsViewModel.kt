package com.example.neptune.ui.views.modeSettingsView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.ViewsCollection

class ModeSettingsViewModel() : ViewModel() {

    private var playlistLinkInput by mutableStateOf("")

    private var sliderPosition by mutableFloatStateOf(0f)


    fun isPlaylistLinkInputAvailable(): Boolean {
        //TODO
        return false
    }

    fun getCurrentPlaylistLinkInput(): String {
        return playlistLinkInput
    }

    fun onPlaylistLinkInputChange(newInput: String) {
        playlistLinkInput = newInput
    }

    fun isPlaylistLinkValid(): Boolean {
        //TODO
        return false
    }

    fun getCooldownSliderPosition(): Float {
        return sliderPosition
    }

    fun onCooldownSliderPositionChange(newPosition: Float) {
        sliderPosition = newPosition
    }

    fun onCooldownSliderFinish() {
        //TODO
    }

    fun getTrackCooldownString(): String {
        //TODO
        return "INF"
    }

    fun isArtistSearchAvailable(): Boolean {
        //TODO
        return false
    }

    fun onArtistSearch(navController: NavController) {
        //TODO
    }

    fun getSelectedArtists(): List<String> {
        //TODO
        return listOf()
    }

    fun isGenreSearchAvailable(): Boolean {
        //TODO
        return false
    }

    fun onGenreSearch(navController: NavController) {
        //TODO
    }

    fun getSelectedGenres(): List<String> {
        //TODO
        return listOf()
    }

    fun onToggleSelect(entityName: String) {
        //TODO
    }

    fun onConfirmSettings(navController: NavController) {
        //TODO
        navController.navigate(ViewsCollection.CONTROL_VIEW.name)
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}