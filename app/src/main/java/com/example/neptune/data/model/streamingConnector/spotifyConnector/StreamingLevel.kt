package com.example.neptune.data.model.streamingConnector.spotifyConnector

/**
 * Enum class representing different levels of streaming access.
 */
enum class StreamingLevel {
    /** Represents a premium streaming subscription level. */
    PREMIUM,

    /** Represents a free streaming subscription level. */
    FREE,

    /** Represents a streaming level when the account is not linked. */
    UNLINKED,

    /** Represents an undetermined streaming level. */
    UNDETERMINED
}