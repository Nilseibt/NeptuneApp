package com.example.neptune.ui.views.modeSettingsView

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class ModeSettingsViewModel() : ViewModel() {


    fun isPlaylistLinkInputAvailable(): Boolean {
        //TODO
        return false
    }

    fun getPlaylistLinkInput(): String {
        //TODO
        return "PLACEHOLDER"
    }

    fun onPlaylistLinkInputChange(newInput: String) {
        //TODO
    }

    fun isPlaylistLinkValid(): Boolean {
        //TODO
        return false
    }

    fun getCooldownSliderPosition(): Float {
        //TODO
        return 1f
    }

    fun onCooldownSliderPositionChange(newPosition: Float) {
        //TODO
    }

    fun onCooldownSliderFinish(position: Float) {
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
    }

    fun onBack(navController: NavController) {
        //TODO
    }

}