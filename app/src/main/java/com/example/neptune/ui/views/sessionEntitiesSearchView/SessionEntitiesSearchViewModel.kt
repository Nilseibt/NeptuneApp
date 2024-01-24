package com.example.neptune.ui.views.sessionEntitiesSearchView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class SessionEntitiesSearchViewModel() : ViewModel() {

    private var searchInput by mutableStateOf("")

    fun getSearchInput(): String {
        //TODO
        return "SEARCHESSS"
    }

    fun onSearchInputChange(newInput: String) {
        //TODO
    }

    fun onToggleSelect(entityName: String) {
        //TODO
    }

    fun getEntitiesList(): List<String> {
        //TODO
        return listOf()
    }

    fun onBack(navController: NavController) {
        navController.popBackStack()
    }

}