package com.example.neptune.data.model.appState

import android.content.Context
import android.net.wifi.WifiManager
import com.example.neptune.data.model.session.SessionBuilder
import com.example.neptune.data.model.streamingConnector.StreamingEstablisher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AppState(
    val streamingEstablisher: StreamingEstablisher,
    var sessionBuilder: SessionBuilder,
    private val appDatabase: AppDatabase,
    val context: Context
) {


    private var deviceId = ""

    init {
        GlobalScope.launch {
            streamingEstablisher.restoreConnectionIfPossible()
        }
        GlobalScope.launch {
            generateOrRetrieveDeviceId()
        }
    }





    private suspend fun generateOrRetrieveDeviceId() {
        if (appDatabase.hasDeviceId()) {
            deviceId = appDatabase.getDeviceId()
        } else {
            deviceId = generateDeviceId()
            appDatabase.setDeviceId(deviceId)
        }
    }

    private fun generateDeviceId(): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val macAddress = wifiManager.connectionInfo.macAddress
        val randomAddition = (0..Int.MAX_VALUE).random().toString()
        return generateSHA256(macAddress + randomAddition)
    }

    private fun generateSHA256(input: String): String {
        val bytes = input.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        val hexString = StringBuffer()
        for (byte in digest) {
            val hex = Integer.toHexString(0xff and byte.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}