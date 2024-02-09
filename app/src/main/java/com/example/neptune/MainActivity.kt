package com.example.neptune

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.neptune.ui.NeptuneNavGraph
import android.content.Intent
import android.net.Uri


/**
 * The main activity of the application.
 * Responsible for setting the content of the activity to the NeptuneNavGraph,
 * which defines the navigation graph for the application and displays it.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is first created.
     * Sets the content of the activity to the NeptuneNavGraph,
     * defining and displaying the navigation graph for the application.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeptuneNavGraph(this)
        }
    }
}