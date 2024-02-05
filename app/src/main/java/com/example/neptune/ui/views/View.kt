package com.example.neptune.ui.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.neptune.MainActivity

data class View(
    val name: String, // is there because the navHost only works with strings
    val Composable: @Composable (navController: NavController) -> Unit = {},
    val ComposableWithActivity: @Composable (navController: NavController, activity: MainActivity)
    -> Unit = { navController: NavController, mainActivity: MainActivity -> }
)