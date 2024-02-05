package com.example.neptune.ui.views

import com.example.neptune.ui.views.controlView.ControlView
import com.example.neptune.ui.views.infoView.InfoView
import com.example.neptune.ui.views.joinView.JoinView
import com.example.neptune.ui.views.modeSelectView.ModeSelectView
import com.example.neptune.ui.views.modeSettingsView.ModeSettingsView
import com.example.neptune.ui.views.searchView.SearchView
import com.example.neptune.ui.views.sessionEntitiesSearchView.SessionEntitiesSearchView
import com.example.neptune.ui.views.startView.StartView
import com.example.neptune.ui.views.statsView.StatsView
import com.example.neptune.ui.views.voteView.VoteView


object ViewsCollection {
    val NAV_VIEW =
        View(
            name = "NAV_VIEW",
            Composable = { navController -> NavView(navController) }
        )
    val START_VIEW =
        View(
            name = "START_VIEW",
            ComposableWithActivity = { navController, activity -> StartView(navController, activity) }
        )
    val JOIN_VIEW =
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
        )
}