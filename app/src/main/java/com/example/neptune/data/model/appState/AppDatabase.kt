package com.example.neptune.data.model.appState

import com.example.neptune.data.room.app.AppData
import com.example.neptune.data.room.app.AppDataDao


/**
 * Wrapper for the database for storing application state-related data, which is only the deviceId.
 * @property appDataDao The Data Access Object (DAO) for accessing AppData entities in the actual room database.
 */
class AppDatabase(
    private val appDataDao: AppDataDao
) {


    /**
     * Checks if the device ID exists in the database.
     * @return true if the device ID exists, false otherwise.
     */
    suspend fun hasDeviceId(): Boolean {
        return appDataDao.entryCount() != 0
    }

    /**
     * Sets the device ID in the database if it does not already exist.
     * @param deviceId The device ID to be set.
     * @throws Exception if the device ID is already set.
     */
    suspend fun setDeviceId(deviceId: String) {
        if (!hasDeviceId()) {
            appDataDao.upsert(AppData(0, deviceId))
        } else {
            throw Exception("Device Id is already set, cannot be reset")
        }
    }

    /**
     * Retrieves the device ID from the database.
     * @return The device ID.
     * @throws Exception if the device ID is not set.
     */
    suspend fun getDeviceId(): String {
        if (hasDeviceId()) {
            return appDataDao.getDeviceId()
        } else {
            throw Exception("Device Id is not set, but must be already set")
        }
    }

}