package com.example.neptune.data.model.track

import androidx.compose.runtime.MutableState

/**
 * Represents a track with its attributes such as ID, name, artists, genres, and image URL,
 * along with additional state variables like upvotes, upvote status, blocked status, and cooldown status.
 * @param id Unique identifier of the track.
 * @param name Name of the track.
 * @param artists List of artists associated with the track.
 * @param genres Mutable list of genres associated with the track.
 * @param imageUrl URL of the track's image.
 * @param upvotes Mutable state of the upvote count for the track.
 * @param isUpvoted Mutable state representing whether the track has been upvoted by the user.
 * @param isBlocked Mutable state representing whether the track is blocked by the user.
 * @param hasCooldown Mutable state representing whether the track has a cooldown period.
 */
class Track(
    val id: String,
    val name: String,
    val artists: List<String>,
    val genres: MutableList<String>,
    val imageUrl: String,
    private var upvotes: MutableState<Int>,
    private var isUpvoted: MutableState<Boolean>,
    private var isBlocked: MutableState<Boolean>,
    private var hasCooldown: MutableState<Boolean>
) {

    /**
     * Retrieves the current number of upvotes for the track.
     * @return The number of upvotes.
     */
    fun getUpvotes(): Int {
        return upvotes.value
    }

    /**
     * Checks whether the track has been upvoted.
     * @return True if the track has been upvoted, false otherwise.
     */
    fun isUpvoted(): Boolean {
        return isUpvoted.value
    }

    /**
     * Sets the upvote status of the track.
     * @param upvoted True if the track is upvoted, false otherwise.
     */
    fun setUpvoted(upvoted: Boolean) {
        isUpvoted.value = upvoted
    }

    /**
     * Checks whether the track is blocked.
     * @return True if the track is blocked, false otherwise.
     */
    fun isBlocked(): Boolean {
        return isBlocked.value
    }

    /**
     * Sets the blocked status of the track.
     * @param blocked True if the track is blocked, false otherwise.
     */
    fun setBlocked(blocked: Boolean) {
        isBlocked.value = blocked
    }

    /**
     * Checks whether the track has a cooldown period.
     * @return True if the track has a cooldown period, false otherwise.
     */
    fun hasCooldown(): Boolean {
        return hasCooldown.value
    }

    /**
     * Toggles the upvote status of the track and updates the upvote count accordingly.
     */
    fun toggleUpvote() {
        if (isUpvoted()) {
            upvotes.value--
        } else {
            upvotes.value++
        }
        isUpvoted.value = !isUpvoted.value
    }

    /**
     * Retrieves a string representation of the artists associated with the track.
     * @return A string containing the names of the artists.
     */
    fun getArtistNames(): String {
        var artistNames = ""
        artists.forEach {
            artistNames += "$it, "
        }
        return artistNames.substring(0, artistNames.length - 2)
    }

    /*override fun toString(): String {
        return "Id:        $id\n" +
                "name:     $name\n" +
                "artists   $artists\n" +
                "genres    $genres\n" +
                "imageUrl  $imageUrl\n" +
                "upvotes   $upvotes\n" +
                "isUpvoted $isUpvoted\n" +
                "isBlocked $isBlocked"
    }*/
}
