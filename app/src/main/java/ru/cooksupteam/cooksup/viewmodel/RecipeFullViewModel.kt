package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.scope
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.app.uvm
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.model.RecipeFullRemote

class RecipeFullViewModel {
    var isAllDataReady = mutableStateOf(false)
    private var all = listOf<RecipeFullRemote>()
    var allRecipeFull = mutableStateListOf<RecipeFull>()
    var isFavoriteDataReady = mutableStateOf(false)
    private var favoriteRecipeRemote = listOf<RecipeFullRemote>()
    var favoriteRecipeFull = mutableStateListOf<RecipeFull>()
    var lastIndexRecipe = 0

    fun loadFavorite() {
        isFavoriteDataReady.value = false
        scope.launch {
            favoriteRecipeRemote = RESTAPI.fetchFavoriteRecipes(uvm.user.id)
            favoriteRecipeFull.clear()
            favoriteRecipeFull.addAll(favoriteRecipeRemote.map {
                RecipeFull(
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

    fun load(search: String = "") {
        isAllDataReady.value = false
        scope.launch {
            if (search.isNotEmpty()) {
                all = RESTAPI.fetchRecipeFilteredFromText(search.replace("ё", "е").trim())
                allRecipeFull.clear()
                allRecipeFull.addAll(all.map {
                    RecipeFull(
                        id = it.id,
                        name = it.name,
                        description = it.description,
//                        pic = "http://${ip}:${port}/recipes_pics/" + it.name + ".webp",
                        pic = it.pic,
                        nutrition = it.nutrition,
                        time = it.time,
                        servings = it.servings,
                        quantityIngredients = it.quantityIngredients,
                        instructions = it.instructions
                    )
                })
            } else if (ivm.selectedIngredients.isNotEmpty()) {
                if (ivm.lastIngredients != ivm.selectedIngredients.map { it.name }) {
                    all = RESTAPI.fetchRecipeFiltered(ivm.selectedIngredients.map { it.name })
                    allRecipeFull.clear()
                    allRecipeFull.addAll(all.map {
                        RecipeFull(
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

                    ivm.lastIngredients = ivm.selectedIngredients.map { it.name }
                }
            }
            isAllDataReady.value = true
        }
    }
}