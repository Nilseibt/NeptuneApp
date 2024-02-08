import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.neptune.ui.theme.White
import com.example.neptune.ui.views.joinView.JoinViewModel
import com.example.neptune.ui.views.modeSettingsView.ModeSettingsViewModel

/**
 * The Composable for an OutlinedTextField used in the JoinView.
 *
 * @param joinViewModel The ViewModel of the JoinView the Composable is used in
 * @param labelText The text to be displayed as the TextField's label, depending on the situation
 * @param modifier The modifier used for the TextField
 * @param focusedBorderColor The Color of the TextField's border when the user interacts with it
 * @param focusedLabelColor The Color of the TextField's label when the user interacts with it
 * @param textStyle Enables to set the TextColor for the text entered in the TextField
 */
@Composable
fun NeptuneOutlinedTextField(
    joinViewModel: JoinViewModel,
    labelText: String,
    modifier: Modifier = Modifier,
    focusedBorderColor: Color = White,
    unfocusedBorderColor: Color = Color.Gray,
    focusedLabelColor: Color = White,
    textStyle: TextStyle = TextStyle(color = White),
) {
    OutlinedTextField(
        value = joinViewModel.getCodeInput(),
        onValueChange = { joinViewModel.onCodeInputChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxWidth().padding(end = 8.dp),
        label = { Text(text = labelText, color = focusedLabelColor) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            cursorColor = White
        ),
        textStyle = textStyle
    )
}

/**
 * The Composable for an OutlinedTextField used in the JoinView.
 *
 * @param modeSettingsViewModel The ViewModel of the JoinView the Composable is used in
 * @param labelText The text to be displayed as the TextField's label, depending on the situation
 * @param modifier The modifier used for the TextField
 * @param focusedBorderColor The Color of the TextField's border when the user interacts with it
 * @param focusedLabelColor The Color of the TextField's label when the user interacts with it
 * @param textStyle Enables to set the TextColor for the text entered in the TextField
 */
@Composable
fun NeptuneOutlinedTextField(
    modeSettingsViewModel: ModeSettingsViewModel,
    labelText: String,
    modifier: Modifier = Modifier,
    focusedBorderColor: Color = White,
    unfocusedBorderColor: Color = Color.Gray,
    focusedLabelColor: Color = White,
    textStyle: TextStyle = TextStyle(color = White)
) {
    OutlinedTextField(
        value = modeSettingsViewModel.getCurrentPlaylistLinkInput(),
        onValueChange = { modeSettingsViewModel.onPlaylistLinkInputChange(it) },
        modifier = modifier.fillMaxWidth().padding(end = 8.dp),
        label = { Text(text = labelText, color = focusedLabelColor) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            cursorColor = White
        ),
        textStyle = textStyle
    )
}
