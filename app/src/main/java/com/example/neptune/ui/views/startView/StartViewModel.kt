package com.example.neptune.ui.views.startView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.ViewsCollection

class StartViewModel() : ViewModel() {


    private var leaveDialogShown by mutableStateOf(false)

    fun createSessionPossible(): Boolean {
        //TODO
        return false
    }

    fun onToggleConnectedToSpotify() {
        //TODO
    }

    fun getSpotifyButtonText(): String {
        //TODO
        return "Spotify Toggle"
    }

    fun onJoinSession(navController: NavController) {
        navController.navigate(ViewsCollection.JOIN_VIEW.name)
    }

    fun onCreateSession(navController: NavController) {
        navController.navigate(ViewsCollection.MODE_SELECT_VIEW.name)
    }

    fun isLeaveDialogShown(): Boolean {
        return leaveDialogShown
    }

    fun onBack(navController: NavController) {
        leaveDialogShown = !leaveDialogShown
    }

    fun onConfirmLeave(navController: NavController) {
        leaveDialogShown = false
        navController.popBackStack()
    }

    fun onDismissLeave(navController: NavController) {
        leaveDialogShown = false
    }

}