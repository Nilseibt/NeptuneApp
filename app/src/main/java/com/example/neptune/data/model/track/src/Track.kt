package com.example.neptune.data.model.track.src

import java.sql.Timestamp
import  java.util.Date

class Track(
    val id: String,
    val name: String,
    val artists: List<String>,
    val genres: List<String>,
    val imageUrl: String,
    var upvotes: Int,
    var isUpvoted: Boolean,
    var isBlocked: Boolean
) {
    fun addUpvote() {
        upvotes += 1
    }

    fun removeUpvote() {
        if (upvotes > 0) {
            upvotes -= 1
        }
    }

    fun getArtistNames(): String{
        var artistNames = ""
        artists.forEach {
            artistNames += "$it, "
        }
        return artistNames.substring(0, artistNames.length - 2)
    }

    override fun toString(): String {
        return "Id:        $id\n" +
                "name:     $name\n" +
                "artists   $artists\n"+
                "genres    $genres\n"+
                "imageUrl  $imageUrl\n"+
                "upvotes   $upvotes\n"+
                "isUpvoted $isUpvoted\n"+
                "isBlocked $isBlocked"
    }
}
