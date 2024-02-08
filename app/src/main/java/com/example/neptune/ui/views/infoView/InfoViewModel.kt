package com.example.neptune.ui.views.infoView

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.data.model.session.ArtistSession
import com.example.neptune.data.model.session.GenreSession
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.data.model.user.User
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter


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
        val shareLink = "https://nep-tune.de/join/" + user.session.id.toString()
        ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setText(shareLink)
            .setChooserTitle("Share Session Code")
            .startChooser()
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

    @Throws(WriterException::class)
    fun getQRCode(): ImageBitmap {
        val writer = QRCodeWriter()
        val bitMatrix =
            writer.encode("http://nep-tune.de/join/${user.session.id}", BarcodeFormat.QR_CODE, 300, 300)
        val w = bitMatrix.width
        val h = bitMatrix.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                pixels[y * w + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap.asImageBitmap()
    }

}