package com.example.neptune.ui.views.infoView

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class InfoViewModel() : ViewModel() {

    fun getModeName(): String {
        //TODO
        return "THE MODE"
    }

    fun isArtistMode(): Boolean {
        //TODO
        return false
    }

    fun getAllArtists(): String {
        //TODO
        return "THE ARTISTS"
    }

    fun isGenreMode(): Boolean {
        //TODO
        return false
    }

    fun getAllGenres(): String {
        //TODO
        return "THE GENRES"
    }

    fun getSessionCode(): String {
        //TODO
        return "123456"
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

    fun onOpenStats(navController: NavController) {
        //TODO
    }

    fun onBack(navController: NavController) {
        //TODO
    }

}