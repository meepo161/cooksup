package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.allRecipeFull
import ru.cooksupteam.cooksup.Singleton.lastIndex
import ru.cooksupteam.cooksup.Singleton.lastIngredients
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.model.RecipeFullRemote

class RecipeFullViewModel(private var listIngredients: List<String>) {
    var all = listOf<RecipeFullRemote>()
    var isDataReady = mutableStateOf(false)

    private val scope = CoroutineScope(Dispatchers.Default)

    fun load(search: String = "") {
        isDataReady.value = false
        scope.launch {
            if (lastIngredients != listIngredients || allRecipeFull.isEmpty()) {
                allRecipeFull.clear()
                if (search.isNotEmpty() && listIngredients.isEmpty()) {
                    all = RESTAPI.fetchRecipeFilteredFromText(search)
                    allRecipeFull.addAll(all.map {
                        RecipeFull(
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
                } else {
                    lastIndex = 0
                    allRecipeFull.clear()
                        all = RESTAPI.fetchRecipeFiltered(listIngredients)
                        allRecipeFull.addAll(all.map {
                            RecipeFull(
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
                lastIngredients = listIngredients
            }
            isDataReady.value = true
        }
    }
}