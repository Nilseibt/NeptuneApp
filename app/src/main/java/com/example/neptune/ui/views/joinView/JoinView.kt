package com.example.neptune.ui.views.joinView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
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

    Column {
        TextField(
            value = joinViewModel.getCodeInput(),
            onValueChange = { joinViewModel.onCodeInputChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(
            onClick = { joinViewModel.onConfirmSessionCode(navController) },
            enabled = joinViewModel.isCodeInputFormValid()
        ) {
            Text(text = "->")
        }
        Button(
            onClick = { joinViewModel.onScanQrCode() },
        ) {
            Text(text = "QR-Code scannen")
        }
    }
}