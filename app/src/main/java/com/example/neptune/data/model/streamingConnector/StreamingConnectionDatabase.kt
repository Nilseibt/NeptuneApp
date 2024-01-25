package com.example.neptune.data.model.streamingConnector


interface StreamingConnectionDatabase {

    suspend fun hasLinkedEntry(): Boolean

    suspend fun isLinked(): Boolean

    suspend fun setLinked(isLinked: Boolean)

    suspend fun getRefreshToken(): String

    suspend fun setRefreshToken(refreshToken: String)

}