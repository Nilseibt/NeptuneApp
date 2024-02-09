package com.example.neptune.ui.views.joinView

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.appState.AppState

/**
 * ViewModel class for controlling the logic of the JoinView.
 *
 * @property appState The current app state.
 */
class JoinViewModel(
    val appState: AppState
) : ViewModel() {

    private var sessionCodeInput by mutableStateOf("")

    private var lastCodeInvalid by mutableStateOf(false)

    /**
     * Retrieves the current session code input.
     *
     * @return The session code input.
     */
    fun getCodeInput(): String {
        return sessionCodeInput
    }

    /**
     * Checks if the session code input form is valid.
     *
     * @return True if the session code input form is valid, false otherwise.
     */
    fun isCodeInputFormValid(): Boolean {
        return sessionCodeInput.length == 6
    }

    /**
     * Checks if the last entered session code was invalid.
     *
     * @return True if the last entered session code was invalid, false otherwise.
     */
    fun wasLastCodeInvalid(): Boolean {
        return lastCodeInvalid
    }

    /**
     * Handles changes in the session code input.
     *
     * @param newInput The new input for the session code.
     */
    fun onCodeInputChange(newInput: String) {
        if (newInput.length <= 6
            && (newInput.toIntOrNull()?.let { true } == true || newInput == "")
        ) {
            sessionCodeInput = newInput
            lastCodeInvalid = false
        }
    }

    /**
     * Attempts to confirm the session code.
     * Tries to join the session with the provided code and navigates accordingly.
     *
     * @param navController The navigation controller to handle navigation.
     */
    fun onConfirmSessionCode(navController: NavController) {
        NeptuneApp.model.appState.tryToJoinSession(sessionCodeInput.toInt(), navController) {
            lastCodeInvalid = true
        }
    }

    /**
     * Handles the detection of a QR code.
     *
     * If a valid session code is found in the QR code text, it attempts to join the session
     * and navigates accordingly.
     *
     * @param navController The navigation controller to handle navigation.
     * @param qrCodeText The text extracted from the QR code.
     */
    fun onQRCodeDetected(navController: NavController, qrCodeText: String) {
        val shareLinkPattern = Regex("""http://nep-tune.de/join/(\d{6})""")
        val sessionCodeMatchResult = shareLinkPattern.find(qrCodeText)
        if (sessionCodeMatchResult != null) {
            NeptuneApp.model.appState.tryToJoinSession(sessionCodeMatchResult.groupValues[1].toInt(), navController) {
                lastCodeInvalid = true
            }
        }

    }

    /**
     * Handles the back action.
     * Pops the back stack of the navigation controller.
     *
     * @param navController The navigation controller to perform the back action.
     */
    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}