package com.example.neptune

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Activity responsible for handling the authorization callback from Spotify.
 * When created or receiving a new intent, it extracts the authorization code from the intent's data
 * and passes it to the streaming establisher to complete the Spotify connection process.
 */
class AuthCallbackActivity : ComponentActivity() {

    /**
     * Called when the activity is first created.
     * Handles the intent and finishes the activity immediately after processing.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        finish()
    }

    /**
     * Called when a new intent is received.
     * Handles the new intent and finishes the activity immediately after processing.
     *
     * @param intent The new intent received by the activity.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
        finish()
    }

    /**
     * Handles the received intent, extracting the authorization code from the intent's data.
     * If an authorization code is found, it initiates the completion of the Spotify connection process.
     *
     * @param intent The intent received by the activity.
     */
    fun handleIntent(intent: Intent){
        Log.i("RETURN CODE OAUTH", "is called from spotify")
        val code = intent.data?.getQueryParameter("code")
        if(code != null){
            GlobalScope.launch {
                NeptuneApp.model.appState.streamingEstablisher.finishConnectWithCode(code)
            }
        }
    }
}