package com.example.neptune.ui.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neptune.data.model.track.Track

/**
 * The composable for a track list.
 *
 * @param tracks The tracks of this list.
 * @param trackListType The list type of this list.
 * @param onToggleUpvote The function for the upvote button of the tracks.
 * @param onToggleDropdown The function for the dropdown button of the tracks.
 * @param isDropdownExpanded The function that says if the dropdown menu of a track is expanded.
 * @param onAddToQueue The function that adds a track to the queue.
 * @param onRemoveFromQueue The function that removes a track from the queue.
 * @param onToggleBlock The function that locks and unlocks a track.
 * @param onMoveUp The function for moving a track upwards in the queue.
 * @param onMoveDown The function for moving a track downwards in the queue.
 */
@Composable
fun TrackListComposable(
    tracks: List<MutableState<Track>>,
    trackListType: TrackListType,
    onToggleUpvote: (Track) -> Unit,
    onToggleDropdown: (index: Int) -> Unit = {},
    isDropdownExpanded: (index: Int) -> Boolean = { false },
    onAddToQueue: (Track) -> Unit = {},
    onRemoveFromQueue: (index: Int) -> Unit = {},
    onToggleBlock: (Track) -> Unit = {},
    onMoveUp: (index: Int) -> Unit = {},
    onMoveDown: (index: Int) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(3.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, fill = false)
        ) {

            for (trackIndex in tracks.indices) {

                TrackComposable(
                    tracks[trackIndex].value,
                    trackIndex,
                    tracks.size,
                    trackListType,
                    onToggleUpvote,
                    onToggleDropdown,
                    isDropdownExpanded,
                    onAddToQueue,
                    onRemoveFromQueue,
                    onToggleBlock,
                    onMoveUp,
                    onMoveDown
                )

            }

        }

    }

}
