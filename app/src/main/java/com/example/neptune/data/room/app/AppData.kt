package com.example.neptune.data.room.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents an entity class for storing application data in the Room database.
 * @param artificialId The artificial ID of the application data.
 * @param deviceId The device ID associated with the application data.
 */
@Entity(tableName = "app_data")
data class AppData(
    @ColumnInfo(name = "artificial_id")
    @PrimaryKey
    val artificialId: Int,
    @ColumnInfo(name = "device_id")
    val deviceId: String
)