package com.example.neptune

import android.app.Application
import android.content.Context
import com.example.neptune.data.model.Model


/**
 * The main Application class for the Neptune app.
 * Responsible for initializing the application-wide resources and components.
 */
class NeptuneApp: Application() {

    /**
     * Companion object holding static properties for the application.
     * It holds a reference to the single instance of the Model and the application context.
     */
    companion object {
        lateinit var model: Model
        lateinit var context: Context
    }

    /**
     * Called when the application is first created.
     * Initializes the application-wide resources and components, such as the Model and application context.
     */
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        model = Model()
    }

}