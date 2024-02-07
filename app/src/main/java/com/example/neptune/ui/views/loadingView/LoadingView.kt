package com.example.neptune.ui.views.loadingView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.neptune_logo),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
                    .weight(3f)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

        }

    }

}
