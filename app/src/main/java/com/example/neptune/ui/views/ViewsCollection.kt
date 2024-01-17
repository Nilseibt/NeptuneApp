package com.example.neptune.ui.views

import com.example.neptune.ui.views.startView.StartView


object ViewsCollection {
    val START_VIEW =
        View(
            name = "START_VIEW",
            Composable = { navController -> StartView(navController) }
        )
    /*val JOIN_VIEW =
        View(
            name = "JOIN_VIEW",
            Composable = { navController -> JoinView(navController) }
        )
    val VOTE_VIEW =
        View(
            name = "VOTE_VIEW",
            Composable = { navController -> VoteView(navController) }
        )
    val SEARCH_VIEW =
        View(
            name = "SEARCH_VIEW",
            Composable = { navController -> SearchView(navController) }
        )
    val MODE_SELECT_VIEW =
        View(
            name = "MODE_SELECT_VIEW",
            Composable = { navController -> ModeSelectView(navController) }
        )
    val MODE_SETTINGS_VIEW =
        View(
            name = "MODE_SETTINGS_VIEW",
            Composable = { navController -> ModeSettingsView(navController) }
        )
    val CONTROL_VIEW =
        View(
            name = "CONTROL_VIEW",
            Composable = { navController -> ControlView(navController) }
        )
    val INFO_VIEW =
        View(
            name = "INFO_VIEW",
            Composable = { navController -> InfoView(navController) }
        )
    val STATS_VIEW =
        View(
            name = "STATS_VIEW",
            Composable = { navController -> StatsView(navController) }
        )
    val SESSION_ENTITIES_SEARCH_VIEW =
        View(
            name = "SESSION_ENTITIES_SEARCH_VIEW",
            Composable = { navController -> SessionEntitiesSearchView(navController) }
        )*/
}