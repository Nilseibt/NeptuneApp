package com.example.neptune.ui.views.infoView

import android.app.Activity
import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.data.model.session.ArtistSession
import com.example.neptune.data.model.session.GenreSession
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.user.src.User


class InfoViewModel(
    val user: User,
    val activity: MainActivity
) : ViewModel() {

    fun getMode(): SessionType {
        return user.session.sessionType
    }

    fun isArtistMode(): Boolean {
        return user.session.sessionType == SessionType.ARTIST
    }

    fun getAllArtists(): List<String> {
        return (user.session as ArtistSession).artists
    }

    fun isGenreMode(): Boolean {
        return user.session.sessionType == SessionType.GENRE
    }

    fun getAllGenres(): List<String> {
        return (user.session as GenreSession).genres
    }

    fun getSessionCode(): String {
        return user.session.id.toString()
    }

    fun onShareLink() {
        val shareText = "Neptune Session Code: " + user.session.id.toString()
        ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setText(shareText)
            .setChooserTitle("Share Session Code")
            .startChooser()
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}