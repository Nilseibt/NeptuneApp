package com.example.neptune.ui.views.loadingView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.appState.AppState
import com.example.neptune.ui.views.ViewsCollection
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
                val joinLinkUsed = (sessionCode != null)
                NeptuneApp.model.appState.recreateUserSessionStateInitially(navController, joinLinkUsed) {
                    if(joinLinkUsed) {
                        if (sessionCode!!.toIntOrNull()?.let { true } == true
                            && sessionCode.length == 6
                        ) {
                            NeptuneApp.model.appState.tryToJoinSession(
                                sessionCode.toInt(),
                                navController
                            ){
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

    fun onBack(navController: NavController) {
        activity.finish()
    }
}
