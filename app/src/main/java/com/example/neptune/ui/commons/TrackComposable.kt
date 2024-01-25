package com.example.neptune.ui.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.neptune.model.track.src.Track

@Composable
fun TrackComposable(
    track: Track,
    trackIndexInList: Int,
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color(0xFF2D323E))
            .clip(shape = RoundedCornerShape(3.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .padding(5.dp)
        ) {
            AsyncImage(
                model = track.imageUrl,
                contentDescription = "track image"
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(5.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    track.name,
                    color = Color(0xFFF9FAFD),
                    fontSize = 18.sp,
                    maxLines = 1
                )
                Text(
                    track.getArtistNames(),
                    color = Color(0xFFABADC2),
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
        }

        if (trackListType == TrackListType.HOST_QUEUE) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1.5f)
            ) {
                IconButton(onClick = { onMoveUp(trackIndexInList) }) {
                    Icon(Icons.Default.KeyboardArrowUp, "", tint = Color.White)
                }
                IconButton(onClick = { onMoveDown(trackIndexInList) }) {
                    Icon(Icons.Default.KeyboardArrowDown, "", tint = Color.White)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.7f)
                .padding(5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                Text(track.upvotes.toString(), color = Color(0xFF12B1BE))
                IconButton(onClick = { onToggleUpvote(track) }) {
                    var icon: ImageVector
                    if (track.isUpvoted) {
                        icon = Icons.Default.CheckCircle
                    } else {
                        icon = Icons.Default.AddCircle
                    }
                    Icon(icon, "", tint = Color.White)
                }

                if (trackListType == TrackListType.HOST_QUEUE
                    || trackListType == TrackListType.HOST_VOTE
                    || trackListType == TrackListType.HOST_SEARCH
                ) {

                    IconButton(onClick = { onToggleDropdown(trackIndexInList) }) {
                        Icon(Icons.Default.MoreVert, "", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = isDropdownExpanded(trackIndexInList),
                        onDismissRequest = { onToggleDropdown(trackIndexInList) }) {

                        if (trackListType == TrackListType.HOST_SEARCH || trackListType == TrackListType.HOST_VOTE) {
                            DropdownMenuItem(
                                text = { Text(if (track.isBlocked) "Track entsperren" else "Track sperren") },
                                onClick = { onToggleBlock(track) })
                            DropdownMenuItem(
                                text = { Text("In die Queue") },
                                onClick = { onAddToQueue(track) })
                        }

                        if (trackListType == TrackListType.HOST_QUEUE) {
                            DropdownMenuItem(
                                text = { Text(if (track.isBlocked) "Track entsperren" else "Track sperren") },
                                onClick = { onToggleBlock(track) })
                            DropdownMenuItem(
                                text = { Text("Entfernen") },
                                onClick = { onRemoveFromQueue(trackIndexInList) })
                        }

                    }
                }
            }
        }
    }
}