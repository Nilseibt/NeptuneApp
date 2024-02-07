package com.example.neptune.ui

import androidx.compose.animation.fadeIn
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

    NavHost(navController = navController, startDestination = ViewsCollection.LOADING_VIEW.name) {

        composable(
            ViewsCollection.LOADING_VIEW.name,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://nep-tune.de/join/{argument}"
            }),
        ) { backStackEntry ->
            val argument = backStackEntry.arguments?.getString("argument")
            ViewsCollection.LOADING_VIEW.ComposableWithActivityAndArgument(navController, activity, argument)
        }

        composable(
            route = ViewsCollection.START_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.START_VIEW.ComposableWithActivity(navController, activity)
        }

        composable(
            route = ViewsCollection.JOIN_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.JOIN_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.VOTE_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.VOTE_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.SEARCH_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.SEARCH_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.MODE_SELECT_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.MODE_SELECT_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.MODE_SETTINGS_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.MODE_SETTINGS_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.CONTROL_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.CONTROL_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.INFO_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.INFO_VIEW.ComposableWithActivity(navController, activity)
        }

        composable(
            route = ViewsCollection.STATS_VIEW.name,
            enterTransition = { fadeIn() }
            ) {
            ViewsCollection.STATS_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.name,
            enterTransition = { fadeIn() }
        ) {
            ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.Composable(navController)
        }

    }

}