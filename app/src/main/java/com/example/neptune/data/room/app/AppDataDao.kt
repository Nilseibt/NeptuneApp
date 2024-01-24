package com.example.neptune.data.room.app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AppDataDao{
    @Upsert
    suspend fun upsert(appData: AppData)

    @Query("SELECT COUNT(artificial_id) FROM APP_DATA")
    suspend fun entryCount(): Int

    @Query("UPDATE APP_DATA SET device_id = :deviceId WHERE artificial_id=0")
    suspend fun setDeviceId(deviceId: String)

    @Query("SELECT device_id FROM APP_DATA WHERE artificial_id=0")
    suspend fun getDeviceId(): String
}