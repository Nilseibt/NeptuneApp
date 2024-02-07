package com.example.neptune.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.neptune.MainActivity
import com.example.neptune.ui.views.ViewsCollection

@Composable
fun NeptuneNavGraph(activity: MainActivity) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ViewsCollection.START_VIEW.name) {

        composable(ViewsCollection.NAV_VIEW.name) {
            ViewsCollection.NAV_VIEW.Composable(navController)
        }

        composable(
            ViewsCollection.START_VIEW.name,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://nep-tune.de/join/{argument}"
            }),
        ) { backStackEntry ->
            val parameter = backStackEntry.arguments?.getString("argument")
            Text("Join session: $parameter")
        }

        composable(ViewsCollection.JOIN_VIEW.name) {
            ViewsCollection.JOIN_VIEW.Composable(navController)
        }

        composable(ViewsCollection.VOTE_VIEW.name) {
            ViewsCollection.VOTE_VIEW.Composable(navController)
        }

        composable(ViewsCollection.SEARCH_VIEW.name) {
            ViewsCollection.SEARCH_VIEW.Composable(navController)
        }

        composable(ViewsCollection.MODE_SELECT_VIEW.name) {
            ViewsCollection.MODE_SELECT_VIEW.Composable(navController)
        }

        composable(ViewsCollection.MODE_SETTINGS_VIEW.name) {
            ViewsCollection.MODE_SETTINGS_VIEW.Composable(navController)
        }

        composable(ViewsCollection.CONTROL_VIEW.name) {
            ViewsCollection.CONTROL_VIEW.Composable(navController)
        }

        composable(ViewsCollection.INFO_VIEW.name) {
            ViewsCollection.INFO_VIEW.ComposableWithActivity(navController, activity)
        }

        composable(ViewsCollection.STATS_VIEW.name) {
            ViewsCollection.STATS_VIEW.Composable(navController)
        }

        composable(ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.name) {
            ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.Composable(navController)
        }

    }

}