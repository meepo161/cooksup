package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SearchViewModel(private var ivm: IngredientsViewModel) : ViewModel() {
    private val _searchWidgetState: MutableState<Boolean> = mutableStateOf(value = false)
    val searchWidgetState: State<Boolean> = _searchWidgetState

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    var items = mutableStateListOf(*ivm.allIngredients.sortedBy { it.name.lowercase() }
        .toTypedArray())

    fun updateSearchWidgetState(newValue: Boolean) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    fun filterIngredients() {
        items.clear()
        items.addAll(
            items.filter {
                it.name.lowercase()
                    .contains(searchTextState.value.lowercase())
            })
    }
}
