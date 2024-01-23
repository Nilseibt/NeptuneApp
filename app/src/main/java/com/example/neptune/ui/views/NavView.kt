package com.example.neptune.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun NavView(navController: NavController) {

    Column {

        Button(onClick = { navController.navigate(ViewsCollection.START_VIEW.name) }) {
            Text(text = "startView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.CONTROL_VIEW.name) }) {
            Text(text = "controlView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.INFO_VIEW.name) }) {
            Text(text = "infoView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.JOIN_VIEW.name) }) {
            Text(text = "joinView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.MODE_SELECT_VIEW.name) }) {
            Text(text = "modeSelectView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.MODE_SETTINGS_VIEW.name) }) {
            Text(text = "modeSettingsView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.SEARCH_VIEW.name) }) {
            Text(text = "searchView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.name) }) {
            Text(text = "sessionEntitiesSearchView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.STATS_VIEW.name) }) {
            Text(text = "statsView")
        }
        Button(onClick = { navController.navigate(ViewsCollection.VOTE_VIEW.name) }) {
            Text(text = "voteView")
        }

    }

}