package com.example.neptune.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.neptune.MainActivity
import com.example.neptune.NeptuneApp
import com.example.neptune.ui.views.ViewsCollection
import kotlinx.coroutines.delay


/**
 * Composable function representing the navigation graph of the Neptune application.
 * Responsible for defining the navigation routes and transitions between different views.
 *
 * @param activity The main activity hosting the navigation graph.
 */
@Composable
fun NeptuneNavGraph(activity: MainActivity) {

    val navController = rememberNavController()

    val enterTransition = { EnterTransition.None }
    val exitTransition = { ExitTransition.None }

    NavHost(navController = navController, startDestination = ViewsCollection.LOADING_VIEW.name) {

        composable(
            ViewsCollection.LOADING_VIEW.name,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://nep-tune.de/join/{argument}"
            }),
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) { backStackEntry ->
            val argument = backStackEntry.arguments?.getString("argument")
            ViewsCollection.LOADING_VIEW.ComposableWithActivityAndArgument(navController, activity, argument)
        }

        composable(
            route = ViewsCollection.START_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.START_VIEW.ComposableWithActivity(navController, activity)
        }

        composable(
            route = ViewsCollection.JOIN_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.JOIN_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.VOTE_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.VOTE_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.SEARCH_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.SEARCH_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.MODE_SELECT_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.MODE_SELECT_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.MODE_SETTINGS_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.MODE_SETTINGS_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.CONTROL_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.CONTROL_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.INFO_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.INFO_VIEW.ComposableWithActivity(navController, activity)
        }

        composable(
            route = ViewsCollection.STATS_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
            ) {
            ViewsCollection.STATS_VIEW.Composable(navController)
        }

        composable(
            route = ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.name,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) {
            ViewsCollection.SESSION_ENTITIES_SEARCH_VIEW.Composable(navController)
        }

    }

    // Refreshes the spotify access token every 20 minutes if connected to spotify
    LaunchedEffect(
        key1 = Unit,
        block = {
            while (true) {
                delay(20*60*1000)
                NeptuneApp.model.appState.refreshSpotifyConnection()
            }
        }
    )

}