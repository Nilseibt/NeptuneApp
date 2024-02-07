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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.neptune.ui.theme.White
import com.example.neptune.ui.views.joinView.JoinViewModel
import com.example.neptune.ui.views.searchView.SearchViewModel

@Composable
fun NeptuneOutlinedTextField(
    joinViewModel: JoinViewModel,
    labelText: String,
    modifier: Modifier = Modifier,
    focusedBorderColor: Color = White,
    unfocusedBorderColor: Color = Color.Gray,
    focusedLabelColor: Color = White,
    textStyle: TextStyle = TextStyle(color = White),
    cursorColor: Color = White
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
            cursorColor = cursorColor
        ),
        textStyle = textStyle
    )
}

@Composable
fun NeptuneOutlinedTextField(
    searchViewModel: SearchViewModel,
    labelText: String,
    modifier: Modifier = Modifier,
    focusedBorderColor: Color = White,
    unfocusedBorderColor: Color = Color.Gray,
    focusedLabelColor: Color = White,
    textStyle: TextStyle = TextStyle(color = White),
    cursorColor: Color = White
) {
    OutlinedTextField(
        value = searchViewModel.getTrackSearchInput(),
        onValueChange = { searchViewModel.onTrackSearchInputChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxWidth().padding(end = 8.dp),
        label = { Text(text = labelText, color = focusedLabelColor) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            cursorColor = cursorColor
        ),
        textStyle = textStyle
    )
}
