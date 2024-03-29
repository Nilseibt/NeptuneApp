package com.example.neptune.ui.views.loadingView

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.ui.views.ViewsCollection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * ViewModel class for controlling the logic of the LoadingView.
 *
 * @property appState The current app state.
 * @property navController The navigation controller used for navigating between different views.
 * @property activity The main activity context.
 * @property sessionCode The session code, if provided. Null if not provided.
 */
class LoadingViewModel(
    val appState: AppState,
    val navController: NavController,
    val activity: MainActivity,
    val sessionCode: String?
) : ViewModel() {


    private var loadingFailPopupShown by mutableStateOf(false)


    /**
     * Initializes the loading process and app initialization.
     *
     * This method generates or retrieves the device ID, restores streaming connections if possible,
     * recreates the user session state initially, and deletes irrelevant upvotes.
     */
    init {
        viewModelScope.launch {
            appState.generateOrRetrieveDeviceId()
            appState.streamingEstablisher.restoreConnectionIfPossible {
                val joinLinkUsed = (sessionCode != null)
                NeptuneApp.model.appState.recreateUserSessionStateInitially(
                    navController,
                    joinLinkUsed
                ) {
                    if (joinLinkUsed) {
                        if (sessionCode!!.toIntOrNull()?.let { true } == true
                            && sessionCode.length == 6
                        ) {
                            NeptuneApp.model.appState.tryToJoinSession(
                                sessionCode.toInt(),
                                navController
                            ) {
                                navController.navigate(ViewsCollection.START_VIEW.name)
                            }
                        }
                    }
                }
            }
        }
        GlobalScope.launch {
            NeptuneApp.model.appState.deleteIrrelevantUpvotes()
        }
    }

    /**
     * Set the pop up to show due to a loading fail.
     */
    fun showLoadingFailPopup(){
        loadingFailPopupShown = true
    }

    /**
     * Determines if loading fail pop up is shown.
     *
     * @return true if loading failed and pop up is to be shown.
     */
    fun isLoadingFailPopupShown(): Boolean {
        return loadingFailPopupShown
    }

    /**
     * Handles the back action. Closes the app.
     *
     * @param navController The navigation controller to perform the back action.
     */
    fun onBack(navController: NavController) {
        activity.finish()
    }
}
