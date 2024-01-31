package com.example.neptune.ui.views.joinView

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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.ui.views.util.viewModelFactory

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

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        TextField(
            value = joinViewModel.getCodeInput(),
            onValueChange = { joinViewModel.onCodeInputChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ){
            Button(
                onClick = { joinViewModel.onConfirmSessionCode(navController) },
                enabled = joinViewModel.isCodeInputFormValid()
            )
            {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(
            onClick = { joinViewModel.onScanQrCode() },
        ) {
            Text(text = "QR-Code scannen")
        }
    }
}