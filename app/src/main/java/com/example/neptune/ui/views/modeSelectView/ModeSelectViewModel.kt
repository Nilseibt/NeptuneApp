package com.example.neptune.ui.views.modeSelectView

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.ViewsCollection

class ModeSelectViewModel() : ViewModel() {

    fun isModeSelected(/*mode: Mode*/): Boolean {
        //TODO
        return false
    }

    fun onSelectMode(/*mode: Mode*/){
        //TODO
    }

    fun getSelectedModeDescription(): String{
        //TODO
        return "NONE"
    }

    fun onConfirmMode(navController: NavController) {
        navController.navigate(ViewsCollection.MODE_SETTINGS_VIEW.name)
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}