package com.example.neptune.data.room.app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert


/**
 * Data Access Object (DAO) for accessing AppData entities in the Room database.
 */
@Dao
interface AppDataDao{

    /**
     * Inserts or updates an AppData entity.
     * @param appData The AppData entity to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(appData: AppData)


    /**
     * Retrieves the count of entries in the AppData table.
     * @return The count of entries in the AppData table.
     */
    @Query("SELECT COUNT(artificial_id) FROM APP_DATA")
    suspend fun entryCount(): Int


    /**
     * Sets the device ID in the AppData table.
     * @param deviceId The device ID to be set.
     */
    @Query("UPDATE APP_DATA SET device_id = :deviceId WHERE artificial_id=0")
    suspend fun setDeviceId(deviceId: String)


    /**
     * Retrieves the device ID from the AppData table.
     * @return The device ID retrieved from the AppData table.
     */
    @Query("SELECT device_id FROM APP_DATA WHERE artificial_id=0")
    suspend fun getDeviceId(): String
}