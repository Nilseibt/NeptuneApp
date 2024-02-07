package com.example.neptune.ui.views.loadingView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.appState.AppState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoadingViewModel(
    val appState: AppState,
    val navController: NavController,
    val activity: MainActivity,
    val sessionCode: String?
) : ViewModel() {


    init {
        viewModelScope.launch {
            appState.generateOrRetrieveDeviceId()
            appState.streamingEstablisher.restoreConnectionIfPossible {
                NeptuneApp.model.recreateUserSessionStateInitially(navController) {
                    if (sessionCode != null) {
                        if (sessionCode.toIntOrNull()?.let { true } == true
                            && sessionCode.length == 6
                        ) {
                            NeptuneApp.model.tryToJoinSession(
                                sessionCode.toInt(),
                                navController
                            )
                        }
                    }
                }
            }
        }
        GlobalScope.launch {
            NeptuneApp.model.deleteIrrelevantUpvotes()
        }
    }

    fun onBack(navController: NavController) {
        activity.finish()
    }
}
