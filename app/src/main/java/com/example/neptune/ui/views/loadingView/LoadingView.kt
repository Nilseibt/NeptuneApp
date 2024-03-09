package com.example.neptune.ui.views.loadingView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.R
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
import kotlinx.coroutines.delay

/**
 * The composable for the loadingView.
 *
 * @param navController The NavController needed to navigate to another view.
 * @param activity The main activity context.
 * @param argument The session code, if provided. Null if not provided.
 */
@Composable
fun LoadingView(navController: NavController, activity: MainActivity, argument: String?) {

    val loadingViewModel = viewModel<LoadingViewModel>(
        factory = viewModelFactory {
            LoadingViewModel(
                NeptuneApp.model.appState,
                navController,
                activity,
                argument
            )
        }
    )

    BackHandler {
        loadingViewModel.onBack(navController)
    }

    NeptuneTheme {

        LoadingViewContent()

        // Waits 5 seconds until it shows the popup
        LaunchedEffect(
            key1 = Unit,
            block = {
                delay(5000)
                loadingViewModel.showLoadingFailPopup()
            }
        )

        if(loadingViewModel.isLoadingFailPopupShown()){
            AlertDialog(
                title = { Text(text = stringResource(id = R.string.loading_error)) },
                text = { Text(text = stringResource(id = R.string.loading_error_message)) },
                onDismissRequest = { },
                confirmButton = {
                    TextButton(
                        onClick = { loadingViewModel.onBack(navController) }
                    ) {
                        Text(text = stringResource(id = R.string.accept_text))
                    }
                }
            )
        }

    }

}

@Composable
private fun LoadingViewContent() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.weight(3f)) {
            NeptuneLogo()
        }

        Box(modifier = Modifier.weight(1f)) {
            NeptuneText()
        }

    }


}

@Composable
private fun NeptuneLogo() {

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.neptune_logo),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(100.dp, 20.dp)
        )

    }

}

@Composable
private fun NeptuneText() {

    Text(
        text = stringResource(id = R.string.app_name),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

}