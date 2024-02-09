package com.example.neptune.ui.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.neptune.MainActivity


/**
 * Data class representing a view in the application navigation graph.
 *
 * @property name The name of the view, required because the NavHost only works with strings.
 * @property Composable A composable function in case the view has no any additional arguments.
 * @property ComposableWithActivity A composable function in case the view needs access to the MainActivity.
 * @property ComposableWithActivityAndArgument A composable function in case the view needs access to the MainActivity
 *                                              and an additional argument.
 */
data class View(
    val name: String,

    val Composable: @Composable (navController: NavController) -> Unit = {},

    val ComposableWithActivity: @Composable (navController: NavController, activity: MainActivity)
    -> Unit = { navController: NavController, mainActivity: MainActivity -> },

    val ComposableWithActivityAndArgument: @Composable (navController: NavController, activity: MainActivity, argument: String?)
    -> Unit = { navController: NavController, activity: MainActivity, argument: String? -> }
)