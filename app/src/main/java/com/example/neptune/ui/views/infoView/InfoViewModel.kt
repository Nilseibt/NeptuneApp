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


/**
 * ViewModel class for controlling the logic of the InfoView.
 *
 * @property user The user of the current session.
 * @property activity The main activity context.
 */
class InfoViewModel(
    val user: User,
    val activity: MainActivity
) : ViewModel() {

    /**
     * Retrieves the session mode.
     *
     * @return The session mode.
     */
    fun getMode(): SessionType {
        return user.session.sessionType
    }

    /**
     * Checks if the session mode is artist mode.
     *
     * @return True if the session mode is artist mode, false otherwise.
     */
    fun isArtistMode(): Boolean {
        return user.session.sessionType == SessionType.ARTIST
    }

    /**
     * Retrieves all artists in the session.
     *
     * @return The list of all artists.
     */
    fun getAllArtists(): List<String> {
        return (user.session as ArtistSession).artists
    }

    /**
     * Checks if the session mode is genre mode.
     *
     * @return True if the session mode is genre mode, false otherwise.
     */
    fun isGenreMode(): Boolean {
        return user.session.sessionType == SessionType.GENRE
    }

    /**
     * Retrieves all genres in the session.
     *
     * @return The list of all genres.
     */
    fun getAllGenres(): List<String> {
        return (user.session as GenreSession).genres
    }

    /**
     * Retrieves the session code.
     *
     * @return The session code.
     */
    fun getSessionCode(): String {
        return user.session.sessionId.toString()
    }

    /**
     * Shares the session link.
     *
     * Uses ShareCompat to share the session link via available apps on the device.
     */
    fun onShareLink() {
        val shareLink = "http://nep-tune.de/join/" + user.session.sessionId.toString()
        ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setText(shareLink)
            .setChooserTitle("Share Session Code")
            .startChooser()
    }

    /**
     * Handles the back action.
     * Pops the back stack of the navigation controller.
     *
     * @param navController The navigation controller to perform the back action.
     */
    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

    /**
     * Generates a QR code for the session link.
     *
     * @return The generated QR code as an [ImageBitmap].
     * @throws WriterException If an error occurs during QR code generation.
     */
    @Throws(WriterException::class)
    fun getQRCode(): ImageBitmap {
        val writer = QRCodeWriter()
        val size = 500
        val bitMatrix =
            writer.encode("http://nep-tune.de/join/${user.session.sessionId}",
                BarcodeFormat.QR_CODE, size, size)
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