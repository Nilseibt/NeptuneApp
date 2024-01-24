package com.example.neptune.data.model.streamingConnector



interface StreamingConnectionDatabase{

    fun isLinked(): Boolean

    fun setLinked(isLinked: Boolean)

}