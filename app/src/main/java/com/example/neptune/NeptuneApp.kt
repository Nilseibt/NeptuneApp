package com.example.neptune

import android.app.Application
import android.content.Context
import com.example.neptune.data.model.Model

class NeptuneApp: Application() {

    companion object {
        lateinit var model: Model
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        model = Model()
    }

}