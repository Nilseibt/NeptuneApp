package com.example.neptune.ui.views.joinView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.NeptuneNavGraph
import com.example.neptune.ui.views.ViewsCollection

class JoinViewModel() : ViewModel() {

    private var sessionCodeInput by mutableStateOf("")

    fun getCodeInput(): String {
        return sessionCodeInput
    }

    fun isCodeInputFormValid(): Boolean {
        return sessionCodeInput.length == 6
    }

    fun onCodeInputChange(newInput: String) {
        if (newInput.length <= 6 && newInput.toIntOrNull()?.let { true } == true) {
            sessionCodeInput = newInput
        }
    }

    fun onConfirmSessionCode(navController: NavController) {
        NeptuneApp.model.tryToJoinSession(sessionCodeInput.toInt(), navController)
    }

    fun onScanQrCode() {
        //TODO
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}