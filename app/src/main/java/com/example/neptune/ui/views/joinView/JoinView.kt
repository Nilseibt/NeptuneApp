package com.example.neptune.ui.views.joinView

import NeptuneOutlinedTextField
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.R
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.theme.White
import com.example.neptune.ui.views.util.viewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinView(navController: NavController) {

    val joinViewModel = viewModel<JoinViewModel>(
        factory = viewModelFactory {
            JoinViewModel(
                //NeptuneApp.appState
            )
        }
    )

    BackHandler {
        joinViewModel.onBack(navController)
    }

    NeptuneTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
            ) {


            Column(modifier = Modifier
                .fillMaxSize()
            ) {

                TopBar(onBack = { joinViewModel.onBack(navController) })

            }
            Column (modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){

                NeptuneOutlinedTextField(joinViewModel, labelText = stringResource(id = R.string.enter_six_digit_code))

                //TODO change color and make string resource
                if(joinViewModel.wasLastCodeInvalid()){
                    Text(text = "Session code invalid", color = Color.Red)
                }

                /*
                OutlinedTextField(
                    value = joinViewModel.getCodeInput(),
                    onValueChange = { joinViewModel.onCodeInputChange(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    label = { Text(text = stringResource(id = R.string.enter_six_digit_code)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = White, // Change this to the desired color
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = White,// Change this to the desired color
                        cursorColor = White
                    ),
                    textStyle = TextStyle(White)
                )
                */

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    // This is a guard for prohibiting double clicking the join button
                    var joinButtonEnabled by remember { mutableStateOf(true) }
                    LaunchedEffect(joinButtonEnabled) {
                        if (joinButtonEnabled) {
                            return@LaunchedEffect
                        } else {
                            delay(1000)
                            joinButtonEnabled = true
                        }
                    }
                    Button(
                        onClick = {
                            if (joinButtonEnabled) {
                                joinButtonEnabled = false
                                joinViewModel.onConfirmSessionCode(navController)
                            }
                        },
                        enabled = joinViewModel.isCodeInputFormValid()
                    )
                    {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                //TODO Stephan please take this button out
                /*FilledTonalButton(
                    onClick = { joinViewModel.onScanQrCode() },
                ) {
                    //Text(text = "QR-Code scannen")
                    Text(text = stringResource(id = R.string.scan_qr_code))
                }*/
            }
        }
    }
}