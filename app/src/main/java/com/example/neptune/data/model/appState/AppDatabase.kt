package com.example.neptune.data.model.appState

import com.example.neptune.data.room.app.AppData
import com.example.neptune.data.room.app.AppDataDao


class AppDatabase(
    private val appDataDao: AppDataDao
) {


    suspend fun hasDeviceId(): Boolean {
        return appDataDao.entryCount() != 0
    }

    suspend fun setDeviceId(deviceId: String) {
        if (!hasDeviceId()) {
            appDataDao.upsert(AppData(0, deviceId))
        } else {
            //TODO ist das sinnvoll???
            Exception("Device Id is already set, cannot be reset")
        }
    }

    suspend fun getDeviceId(): String {
        if (hasDeviceId()) {
            return appDataDao.getDeviceId()
        } else {
            //TODO ist das sinnvoll???
            Exception("Device Id is not set, but must be already set")
            return ""
        }
    }

}