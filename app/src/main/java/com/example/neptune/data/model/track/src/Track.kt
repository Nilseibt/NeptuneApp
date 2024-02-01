package com.example.neptune.data.model.track.src

import androidx.compose.runtime.MutableState
import java.sql.Timestamp
import  java.util.Date

class Track(
    val id: String,
    val name: String,
    val artists: List<String>,
    val genres: List<String>,
    val imageUrl: String,
    private var upvotes: MutableState<Int>,
    private var isUpvoted: MutableState<Boolean>,
    private var isBlocked: MutableState<Boolean>,
    private var hasCooldown: MutableState<Boolean>
) {

    fun getUpvotes(): Int {
        return upvotes.value
    }

    fun isUpvoted(): Boolean {
        return isUpvoted.value
    }

    fun setUpvoted(upvoted: Boolean){
        isUpvoted.value = upvoted
    }

    fun isLocked(): Boolean {
        return isBlocked.value || hasCooldown.value
    }

    fun isBlocked(): Boolean {
        return isBlocked.value
    }

    fun setBlocked(blocked: Boolean){
        isBlocked.value = blocked
    }

    fun hasCooldown(): Boolean {
        return hasCooldown.value
    }

    fun toggleUpvote() {
        if (isUpvoted()) {
            upvotes.value--
        } else {
            upvotes.value++
        }
        isUpvoted.value = !isUpvoted.value
    }

    fun getArtistNames(): String {
        var artistNames = ""
        artists.forEach {
            artistNames += "$it, "
        }
        return artistNames.substring(0, artistNames.length - 2)
    }

    override fun toString(): String {
        return "Id:        $id\n" +
                "name:     $name\n" +
                "artists   $artists\n" +
                "genres    $genres\n" +
                "imageUrl  $imageUrl\n" +
                "upvotes   $upvotes\n" +
                "isUpvoted $isUpvoted\n" +
                "isBlocked $isBlocked"
    }
}
