package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.model.RecipeFullRemote

class RecipeFullViewModel() {
    var all = listOf<RecipeFullRemote>()
    var isDataReady = mutableStateOf(false)
    private val scope = CoroutineScope(Dispatchers.Default)
    var allRecipeFull = mutableStateListOf<RecipeFull>()
    var lastIndexRecipe = 0

    fun load(search: String = "") {
        isDataReady.value = false
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
            isDataReady.value = true
        }
    }
}