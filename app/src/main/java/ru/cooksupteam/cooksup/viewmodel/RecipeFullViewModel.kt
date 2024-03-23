package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.scope
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.app.rvm
import ru.cooksupteam.cooksup.app.uvm
import ru.cooksupteam.cooksup.model.Filter
import ru.cooksupteam.cooksup.model.Recipe

class RecipeViewModel {
    lateinit var selectedRecipe: Recipe
    var isAllDataReady = mutableStateOf(false)
    var allRecipes = mutableStateListOf<Recipe>()
    var recipeFiltered = mutableStateListOf<Recipe>()
    var isFavoriteDataReady = mutableStateOf(false)
    private var favoriteRecipeRemote = listOf<Recipe>()
    var favoriteRecipe = mutableStateListOf<Recipe>()
    var lastIndexRecipe = 0
    var searchTextState = mutableStateOf("")

    fun load(search: String = "") {
        isAllDataReady.value = false
        scope.launch {
            while (rvm.allRecipes.isEmpty()) {
                delay(10)
            }
            if (search.isNotEmpty()) {
                recipeFiltered.clear()
                recipeFiltered.addAll(
                    RESTAPI.fetchRecipeFilteredFromText(
                        search.replace("ё", "е").trim()
                    )
                )
            } else if (ivm.selectedIngredients.isNotEmpty()) {
                if (ivm.lastIngredients != ivm.selectedIngredients.map { it.name }) {
                    recipeFiltered.clear()
                    recipeFiltered.addAll(RESTAPI.fetchRecipeFiltered(ivm.selectedIngredients.map { it }))
                    ivm.lastIngredients = ivm.selectedIngredients.map { it.name }
                }
            }
            isAllDataReady.value = true
        }
    }

    fun loadFavorite() {
        isFavoriteDataReady.value = false
        scope.launch {
            favoriteRecipeRemote = RESTAPI.fetchFavoriteRecipes(uvm.user.id)
            favoriteRecipe.clear()
            favoriteRecipe.addAll(favoriteRecipeRemote.map {
                Recipe(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    pic = it.pic,
                    nutrition = it.nutrition,
                    time = it.time,
                    servings = it.servings,
                    quantityIngredients = it.quantityIngredients,
                    instructions = it.instructions
                )
            })
        }
        isFavoriteDataReady.value = true
    }
}