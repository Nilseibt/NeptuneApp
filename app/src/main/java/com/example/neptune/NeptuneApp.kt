package com.example.neptune

import android.app.Application
import com.example.neptune.data.model.Model

class NeptuneApp: Application() {

    companion object {
        lateinit var model: Model
    }

    override fun onCreate() {
        super.onCreate()
        model = Model(applicationContext)
    }

}