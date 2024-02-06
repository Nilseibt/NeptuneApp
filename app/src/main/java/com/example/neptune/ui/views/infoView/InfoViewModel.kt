package com.example.neptune.ui.views.infoView

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.data.model.session.ArtistSession
import com.example.neptune.data.model.session.GenreSession
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.user.src.User
import com.example.neptune.ui.views.ViewsCollection

class InfoViewModel(
    val user: User
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

    fun getQrCodeUrl(): String {
        //TODO
        return "QR url"
    }

    fun getShareLink(): String {
        //TODO
        return "sharelink.com"
    }

    fun onShareLink() {
        //TODO
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}