package com.example.neptune.data.model.streamingConnector


interface StreamingConnectorEstablisher {


    suspend fun restoreConnectionIfPossible()

    fun initiateConnectWithAuthorize()

    fun finishConnectWithCode(code: String)

    fun disconnect()

}