package com.example.neptune.ui.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

data class View(
    val name: String, // is there because the navHost only works with strings
    val Composable: @Composable (navController: NavController) -> Unit
)