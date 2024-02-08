package com.example.neptune.ui.views.joinView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp

class JoinViewModel() : ViewModel() {

    private var sessionCodeInput by mutableStateOf("")

    private var lastCodeInvalid by mutableStateOf(false)

    fun getCodeInput(): String {
        return sessionCodeInput
    }

    fun isCodeInputFormValid(): Boolean {
        return sessionCodeInput.length == 6
    }

    fun wasLastCodeInvalid(): Boolean {
        return lastCodeInvalid
    }

    fun onCodeInputChange(newInput: String) {
        if (newInput.length <= 6
            && (newInput.toIntOrNull()?.let { true } == true || newInput == "")
        ) {
            sessionCodeInput = newInput
            lastCodeInvalid = false
        }
    }

    fun onConfirmSessionCode(navController: NavController) {
        NeptuneApp.model.tryToJoinSession(sessionCodeInput.toInt(), navController) {
            lastCodeInvalid = true
        }
    }

    fun onQRCodeDetected(navController: NavController, qrCodeText: String) {
        if (qrCodeText.length == 6) {
            NeptuneApp.model.tryToJoinSession(qrCodeText.toInt(), navController) {
                lastCodeInvalid = true
            }

        }

    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}