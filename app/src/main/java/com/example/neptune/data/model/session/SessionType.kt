package com.example.neptune.data.model.session

/**
 * Enum class representing the types of sessions supported in the application.
 */
enum class SessionType {
    /** General session type with no specific restrictions. */
    GENERAL,

    /** Session type where tracks are associated with specific artists. */
    ARTIST,

    /** Session type where tracks are associated with specific genres. */
    GENRE,

    /** Session type where tracks are added from a predefined playlist. */
    PLAYLIST
}