package com.example.neptune.ui.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.neptune.R
import com.example.neptune.data.model.track.Track

/**
 * The composable for a track.
 *
 * @param track The track which should be shown.
 * @param trackIndexInList The index of the track in its track list.
 * @param totalTracksInList The total number of tracks in the list of this track.
 * @param trackListType The type of list this track is in.
 * @param onToggleUpvote The function for the upvote button.
 * @param onToggleDropdown The function for the dropdown menu button.
 * @param isDropdownExpanded The function that says if the dropdown menu is expanded.
 * @param onAddToQueue The function that adds the track to the queue.
 * @param onRemoveFromQueue The function that removes the track from the queue.
 * @param onToggleBlock The function that locks and unlocks the track.
 * @param onMoveUp The function for moving this track upwards in the queue.
 * @param onMoveDown The function for moving this track downwards in the queue.
 */
@Composable
fun TrackComposable(
    track: Track,
    trackIndexInList: Int,
    totalTracksInList: Int,
    trackListType: TrackListType,
    onToggleUpvote: (Track) -> Unit,
    onToggleDropdown: (index: Int) -> Unit,
    isDropdownExpanded: (index: Int) -> Boolean,
    onAddToQueue: (Track) -> Unit,
    onRemoveFromQueue: (index: Int) -> Unit,
    onToggleBlock: (Track) -> Unit,
    onMoveUp: (index: Int) -> Unit,
    onMoveDown: (index: Int) -> Unit
) {

    val isLocked = track.isBlocked() || track.hasCooldown()

    val elementColor =
        if (isLocked)
            Color(
                MaterialTheme.colorScheme.onBackground.red,
                MaterialTheme.colorScheme.onBackground.green,
                MaterialTheme.colorScheme.onBackground.blue,
                0.3f
            )
        else
            MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(2f)) {
            TrackImage(track)
        }

        Box(modifier = Modifier.weight(5f)) {
            TrackDescription(track, elementColor)
        }

        if (trackListType == TrackListType.HOST_QUEUE) {

            Box(modifier = Modifier.weight(2f)) {
                TrackMoveButtons(
                    trackIndexInList,
                    elementColor,
                    onMoveUp,
                    onMoveDown,
                    totalTracksInList
                )
            }

        }

        Box(modifier = Modifier.weight(2f)) {
            TrackUpvotes(track, isLocked, elementColor, onToggleUpvote)
        }

        if (trackListType == TrackListType.HOST_QUEUE
            || trackListType == TrackListType.HOST_VOTE
            || trackListType == TrackListType.HOST_SEARCH
        ) {

            Box(modifier = Modifier.weight(1f)) {
                TrackDropdownButton(
                    track,
                    trackIndexInList,
                    trackListType,
                    elementColor,
                    onToggleDropdown,
                    isDropdownExpanded,
                    onAddToQueue,
                    onRemoveFromQueue,
                    onToggleBlock
                )
            }

        }

    }
}

@Composable
private fun TrackImage(track: Track) {

    AsyncImage(
        model = track.imageUrl,
        contentDescription = "",
        modifier = Modifier.padding(5.dp)
    )

}

@Composable
private fun TrackDescription(track: Track, elementColor: Color) {

    Column(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxSize()
    ) {

        Text(
            text = track.name,
            color = elementColor,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            modifier = Modifier.weight(3f)
        )

        Text(
            text = track.getArtistNames(),
            color = elementColor,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier.weight(2f)
        )

    }

}

@Composable
private fun TrackMoveButtons(
    trackIndexInList: Int,
    elementColor: Color,
    onMoveUp: (index: Int) -> Unit,
    onMoveDown: (index: Int) -> Unit,
    totalTracksInList: Int
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {

        Box(modifier = Modifier.weight(1f)) {
            MoveUpButton(trackIndexInList, elementColor, onMoveUp)
        }

        Box(modifier = Modifier.weight(1f)) {
            MoveDownButton(trackIndexInList, elementColor, onMoveDown, totalTracksInList)
        }

    }

}

@Composable
private fun MoveUpButton(
    trackIndexInList: Int,
    elementColor: Color,
    onMoveUp: (index: Int) -> Unit
) {

    val moveUpAvailable = trackIndexInList > 1
    var moveUpIconColor =
        if (moveUpAvailable)
            Color(elementColor.red, elementColor.green, elementColor.blue, elementColor.alpha)
        else
            Color(elementColor.red, elementColor.green, elementColor.blue, 0.2f)

    IconButton(
        onClick = {
            if (moveUpAvailable) {
                onMoveUp(trackIndexInList)
            }
        }
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowUp,
            contentDescription = "",
            tint = moveUpIconColor
        )
    }

}

@Composable
private fun MoveDownButton(
    trackIndexInList: Int,
    elementColor: Color,
    onMoveDown: (index: Int) -> Unit,
    totalTracksInList: Int
) {

    val moveDownAvailable = trackIndexInList > 0 && trackIndexInList < totalTracksInList - 1
    var moveDownIconColor =
        if (moveDownAvailable)
            Color(elementColor.red, elementColor.green, elementColor.blue, elementColor.alpha)
        else
            Color(elementColor.red, elementColor.green, elementColor.blue, 0.2f)

    IconButton(
        onClick = {
            if (moveDownAvailable) {
                onMoveDown(trackIndexInList)
            }
        }
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = "",
            tint = moveDownIconColor
        )
    }

}

@Composable
private fun TrackUpvotes(
    track: Track,
    isLocked: Boolean,
    elementColor: Color,
    onToggleUpvote: (Track) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = track.getUpvotes().toString(),
            color = elementColor,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                if (!isLocked) {
                    onToggleUpvote(track)
                }
            },
            modifier = Modifier.weight(1f),
            enabled = !isLocked
        ) {
            Icon(
                imageVector = if (track.isUpvoted()) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "",
                tint = elementColor
            )
        }

    }

}

@Composable
private fun TrackDropdownButton(
    track: Track,
    trackIndexInList: Int,
    trackListType: TrackListType,
    elementColor: Color,
    onToggleDropdown: (index: Int) -> Unit,
    isDropdownExpanded: (index: Int) -> Boolean,
    onAddToQueue: (Track) -> Unit,
    onRemoveFromQueue: (index: Int) -> Unit,
    onToggleBlock: (Track) -> Unit
) {

    IconButton(
        onClick = { onToggleDropdown(trackIndexInList) },
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "",
            tint = elementColor
        )
    }

    TrackDropdownMenu(
        track,
        trackIndexInList,
        trackListType,
        onToggleDropdown,
        isDropdownExpanded,
        onAddToQueue,
        onRemoveFromQueue,
        onToggleBlock
    )

}

@Composable
private fun TrackDropdownMenu(
    track: Track,
    trackIndexInList: Int,
    trackListType: TrackListType,
    onToggleDropdown: (index: Int) -> Unit,
    isDropdownExpanded: (index: Int) -> Boolean,
    onAddToQueue: (Track) -> Unit,
    onRemoveFromQueue: (index: Int) -> Unit,
    onToggleBlock: (Track) -> Unit
) {

    DropdownMenu(
        expanded = isDropdownExpanded(trackIndexInList),
        onDismissRequest = { onToggleDropdown(trackIndexInList) }
    ) {

        if (trackListType == TrackListType.HOST_SEARCH || trackListType == TrackListType.HOST_VOTE) {

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (track.isBlocked()) stringResource(id = R.string.unlock_track_text)
                        else stringResource(id = R.string.lock_track_text)
                    )
                },
                onClick = { onToggleBlock(track) }
            )

            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.add_track_to_queue_text)) },
                onClick = { onAddToQueue(track) }
            )

        }

        if (trackListType == TrackListType.HOST_QUEUE) {

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (track.isBlocked()) stringResource(id = R.string.unlock_track_text)
                        else stringResource(id = R.string.lock_track_text)
                    )
                },
                onClick = { onToggleBlock(track) }
            )

            if (trackIndexInList != 0) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.remove_track_from_queue_text)) },
                    onClick = { onRemoveFromQueue(trackIndexInList) }
                )
            }

        }

    }

}
