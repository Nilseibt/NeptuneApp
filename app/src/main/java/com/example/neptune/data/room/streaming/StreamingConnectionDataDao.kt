package com.example.neptune.data.room.streaming

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface StreamingConnectionDataDao{
    @Upsert
    suspend fun upsert(streamingConnectionData: StreamingConnectionData)

    @Query("SELECT COUNT(artificial_id) FROM STREAMING_CONNECTION_DATA")
    suspend fun entryCount(): Int

    @Query("UPDATE STREAMING_CONNECTION_DATA SET is_linked = :isLinked WHERE artificial_id=0")
    suspend fun setLinked(isLinked: Boolean)

    @Query("SELECT is_linked FROM STREAMING_CONNECTION_DATA WHERE artificial_id=0")
    suspend fun isLinked(): Boolean

    @Query("UPDATE STREAMING_CONNECTION_DATA SET refresh_token = :newRefreshToken WHERE artificial_id=0")
    suspend fun setRefreshToken(newRefreshToken: String)

    @Query("SELECT refresh_token FROM STREAMING_CONNECTION_DATA WHERE artificial_id=0")
    suspend fun getRefreshToken(): String
}