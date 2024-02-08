package com.example.neptune.data.room.streaming

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert


/**
 * Data Access Object (DAO) for accessing StreamingConnectionData entities in the Room database.
 */
@Dao
interface StreamingConnectionDataDao{

    /**
     * Inserts or updates a StreamingConnectionData entity.
     * @param streamingConnectionData The StreamingConnectionData entity to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(streamingConnectionData: StreamingConnectionData)


    /**
     * Retrieves the count of entries in the StreamingConnectionData table.
     * @return The count of entries in the StreamingConnectionData table.
     */
    @Query("SELECT COUNT(artificial_id) FROM STREAMING_CONNECTION_DATA")
    suspend fun entryCount(): Int


    /**
     * Sets the 'is_linked' value in the StreamingConnectionData table.
     * @param isLinked The value to be set.
     */
    @Query("UPDATE STREAMING_CONNECTION_DATA SET is_linked = :isLinked WHERE artificial_id=0")
    suspend fun setLinked(isLinked: Boolean)


    /**
     * Retrieves the 'is_linked' value from the StreamingConnectionData table.
     * @return The 'is_linked' value retrieved from the StreamingConnectionData table.
     */
    @Query("SELECT is_linked FROM STREAMING_CONNECTION_DATA WHERE artificial_id=0")
    suspend fun isLinked(): Boolean


    /**
     * Sets the 'refresh_token' value in the StreamingConnectionData table.
     * @param newRefreshToken The refresh token to be set.
     */
    @Query("UPDATE STREAMING_CONNECTION_DATA SET refresh_token = :newRefreshToken WHERE artificial_id=0")
    suspend fun setRefreshToken(newRefreshToken: String)


    /**
     * Retrieves the 'refresh_token' value from the StreamingConnectionData table.
     * @return The 'refresh_token' value retrieved from the StreamingConnectionData table.
     */
    @Query("SELECT refresh_token FROM STREAMING_CONNECTION_DATA WHERE artificial_id=0")
    suspend fun getRefreshToken(): String
}