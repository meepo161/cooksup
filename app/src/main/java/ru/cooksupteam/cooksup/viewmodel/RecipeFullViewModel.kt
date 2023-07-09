package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.allRecipeFull
import ru.cooksupteam.cooksup.Singleton.lastIngredients
import ru.cooksupteam.cooksup.Singleton.pageRecipes
import ru.cooksupteam.cooksup.Singleton.selectedIngredients
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.model.RecipeFullRemote

class RecipeFullViewModel() {
    var all = listOf<RecipeFullRemote>()
    var isDataReady = mutableStateOf(false)
    private val scope = CoroutineScope(Dispatchers.Default)

//        allRecipeShort.addAll(all.map {
//            RecipeShort(
//                name = it.name,
//                pic = it.pic,
//                quantityIngredients = it.quantityIngredients
//            )
//        }.sortedBy { it.name })
//    }

    fun load(search: String = "") {
        isDataReady.value = false
        scope.launch {
            if (search.isNotEmpty()) {
                all = RESTAPI.fetchRecipeFilteredFromText(search.replace("ё", "е").trim())
                allRecipeFull.clear()
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
            } else if (selectedIngredients.isNotEmpty()) {
                selectedIngredients.add(Ingredient(pageRecipes.toString()))
                all = RESTAPI.fetchRecipeFiltered(selectedIngredients.map { it.name })
                if (pageRecipes == 1) {
                    allRecipeFull.clear()
                }
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
            isDataReady.value = true
        }
    }
}