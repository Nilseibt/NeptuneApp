package com.example.neptune.data.model.streamingConnector


interface StreamingConnectorEstablisher {

    fun initiateConnect()

    fun finishConnectWithCode(code: String)

    fun disconnect()

}