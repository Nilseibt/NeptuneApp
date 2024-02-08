package com.example.neptune.data.model.session.test

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.neptune.data.model.session.Session
import com.example.neptune.data.model.track.Track
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionTest {


    @Test
    fun testValidation() {
        val session = Session(1, 1, 1)
        val validationResult = session.validateTrack(
            Track(
                "",
                "",
                listOf(),
                mutableListOf(),
                "",
                mutableIntStateOf(0),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false)
            )
        )
        assertTrue(validationResult)
    }
}