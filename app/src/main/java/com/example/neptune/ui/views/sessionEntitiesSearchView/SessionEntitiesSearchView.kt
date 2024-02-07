package com.example.neptune.ui.views.sessionEntitiesSearchView

import NeptuneOutlinedTextField
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.NeptuneApp
import com.example.neptune.NeptuneApp.Companion.context
import com.example.neptune.R
import com.example.neptune.data.model.session.SessionType
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory

@Composable
fun SessionEntitiesSearchView(navController: NavController) {

    val sessionEntitiesSearchViewModel = viewModel<SessionEntitiesSearchViewModel>(
        factory = viewModelFactory {
            SessionEntitiesSearchViewModel(
                NeptuneApp.model.appState
            )
        }
    )

    BackHandler {
        sessionEntitiesSearchViewModel.onBack(navController)
    }

    NeptuneTheme {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(onBack = { sessionEntitiesSearchViewModel.onBack(navController) })

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 144.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = determineSearchText(sessionEntitiesSearchViewModel))
                    Box (modifier = Modifier.weight(1f)) {
                        TextField(
                            value = sessionEntitiesSearchViewModel.getSearchInput(),
                            onValueChange = { sessionEntitiesSearchViewModel.onSearchInputChange(it) })


                        //val artist


                        //NeptuneOutlinedTextField(sessionEntitiesSearchViewModel = sessionEntitiesSearchViewModel, labelText = determineSearchText(sessionEntitiesSearchViewModel))

                        Spacer(modifier = Modifier.height(16.dp))
                    }


                    Box (modifier = Modifier.weight(5f)) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            sessionEntitiesSearchViewModel.getEntitiesSearchList().forEach {

                                val isSelected = sessionEntitiesSearchViewModel.isEntitySelected(it)
                                ElevatedButton(onClick = {
                                    sessionEntitiesSearchViewModel.onToggleSelect(it)
                                }) {
                                    Text(text = it)
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.Clear else Icons.Default.Add,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }

                    /*val itemList = sessionEntitiesSearchViewModel.getEntitiesSearchList()

                    LazyColumn(modifier = Modifier
                        .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        items(itemList) { item ->
                            var isSelected by remember { mutableStateOf(false) }
                            isSelected = sessionEntitiesSearchViewModel.isEntitySelected(item)
                            FilledTonalButton(onClick = {
                                //isSelected = !isSelected
                                sessionEntitiesSearchViewModel.onToggleSelect(item)
                            }) {
                                Text(text = item)
                                //Text(text = if (isSelected) "Selected: $it" else it)
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.Clear else Icons.Default.Add,
                                    contentDescription = null
                                )
                            }
                        }
                    }*/


                    Box (modifier = Modifier.weight(1f)) {
                        Button(
                            onClick = { sessionEntitiesSearchViewModel.onBack(navController) },
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(text = stringResource(id = R.string.finished))
                        }
                    }
                }
            }
        }
    }
}

//TODO: Change method mit entsprechender Getter-Methode vom ViewModel, diese muss dann aber ggf.
// public gemacht werden
fun determineSearchText(sessionEntitiesSearchViewModel: SessionEntitiesSearchViewModel) : String{
    //val sessionType : SessionType =sessionEntitiesSearchViewModel.getSessionType()
    val sessionType = SessionType.ARTIST
    return if (sessionType == SessionType.ARTIST) {
        context.getString(R.string.artist_search)
    } else {
        context.getString(R.string.genre_search)
    }
}