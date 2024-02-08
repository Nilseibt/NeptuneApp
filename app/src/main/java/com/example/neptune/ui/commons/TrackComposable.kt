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
 * @param track
 * @param trackIndexInList
 * @param totalTracksInList
 * @param trackListType
 * @param onToggleUpvote
 * @param onToggleDropdown
 * @param isDropdownExpanded
 * @param onAddToQueue
 * @param onRemoveFromQueue
 * @param onToggleBlock
 * @param onMoveUp
 * @param onMoveDown
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
    //TODO make correct color here
    val backgroundColor = if(isLocked) Color.Gray else MaterialTheme.colorScheme.tertiary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .clip(shape = RoundedCornerShape(10.dp))
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(2f)) {
            TrackImage(track)
        }

        Box(modifier = Modifier.weight(5f)) {
            TrackDescription(track)
        }

        if (trackListType == TrackListType.HOST_QUEUE) {

            Box(modifier = Modifier.weight(2f)) {
                TrackMoveButtons(trackIndexInList, onMoveUp, onMoveDown, totalTracksInList)
            }

        }

        Box(modifier = Modifier.weight(2f)) {
            TrackUpvotes(track, isLocked, onToggleUpvote)
        }

        if (trackListType == TrackListType.HOST_QUEUE
            || trackListType == TrackListType.HOST_VOTE
            || trackListType == TrackListType.HOST_SEARCH) {

            Box (modifier = Modifier.weight(1f)) {
                TrackDropdownButton(
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
private fun TrackDescription(track: Track) {

    Column(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxSize()
    ) {

        Text(
            text = track.name,
            color = MaterialTheme.colorScheme.onTertiary,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            modifier = Modifier.weight(3f)
        )

        Text(
            text = track.getArtistNames(),
            color = MaterialTheme.colorScheme.onTertiary,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier.weight(2f)
        )

    }

}

@Composable
private fun TrackMoveButtons(
    trackIndexInList: Int,
    onMoveUp: (index: Int) -> Unit,
    onMoveDown: (index: Int) -> Unit,
    totalTracksInList: Int
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {

        Box(modifier = Modifier.weight(1f)) {
            MoveUpButton(trackIndexInList, onMoveUp)
        }

        Box(modifier = Modifier.weight(1f)) {
            MoveDownButton(trackIndexInList, onMoveDown, totalTracksInList)
        }

    }

}

@Composable
private fun MoveUpButton(trackIndexInList: Int, onMoveUp: (index: Int) -> Unit) {

    //TODO change color gray to actual color
    val moveUpAvailable = trackIndexInList > 1
    val moveUpIconColor =
        if (moveUpAvailable) MaterialTheme.colorScheme.onTertiary else Color.Gray

    IconButton(
        onClick = { if(moveUpAvailable) { onMoveUp(trackIndexInList) } }
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowUp,
            contentDescription = "",
            tint = moveUpIconColor
        )
    }

}

@Composable
private fun MoveDownButton(trackIndexInList: Int, onMoveDown: (index: Int) -> Unit, totalTracksInList: Int) {

    //TODO change color gray to actual color
    val moveDownAvailable = trackIndexInList > 0 && trackIndexInList < totalTracksInList - 1
    val moveDownIconColor =
        if (moveDownAvailable) MaterialTheme.colorScheme.onTertiary else Color.Gray

    IconButton(
        onClick = { if(moveDownAvailable) { onMoveDown(trackIndexInList) } }
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = "",
            tint = moveDownIconColor
        )
    }

}

@Composable
private fun TrackUpvotes(track: Track, isLocked: Boolean, onToggleUpvote: (Track) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = track.getUpvotes().toString(),
            color = MaterialTheme.colorScheme.onTertiary,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = { if(!isLocked) { onToggleUpvote(track) } },
            modifier = Modifier.weight(1f),
            enabled = !isLocked
        ) {
            Icon(
                imageVector = if (track.isUpvoted()) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }

    }

}

@Composable
private fun TrackDropdownButton(
    track: Track,
    trackIndexInList: Int,
    trackListType: TrackListType,
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
            tint = MaterialTheme.colorScheme.onTertiary
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

            if(trackIndexInList != 0) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.remove_track_from_queue_text)) },
                    onClick = { onRemoveFromQueue(trackIndexInList) }
                )
            }

        }

    }

}
