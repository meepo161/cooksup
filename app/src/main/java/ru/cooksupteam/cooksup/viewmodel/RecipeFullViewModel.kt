package ru.cooksupteam.cooksup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.allRecipeFull
import ru.cooksupteam.cooksup.Singleton.allRecipeShort
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.model.RecipeFullRemote
import ru.cooksupteam.cooksup.model.RecipeShort
import ru.cooksupteam.cooksup.model.RecipeShortRemote

class RecipeFullViewModel() {
    var all = listOf<RecipeFullRemote>()
    var allShort = listOf<RecipeShortRemote>()
    var isDataReady = mutableStateOf(false)

    private val scope = CoroutineScope(Dispatchers.Default)

    fun load() {
        isDataReady.value = false
        Log.d("FILE_READY", isDataReady.value.toString())
        scope.launch {
            allShort = RESTAPI.fetchAllRecipes()
            appendToAllRecipes()
            isDataReady.value = true
            Log.d("FILE_READY", isDataReady.value.toString())
            Log.d("FILE_READY", allRecipeShort.size.toString())
        }
    }

    private fun appendToAllRecipes() {
        allRecipeShort.clear()
        allShort.forEach {
            allRecipeShort.add(
                RecipeShort(
                    name = it.name,
                    pic = it.pic,
                    quantityIngredients = it.quantityIngredients
                )
            )
        }
//        allRecipeShort.addAll(all.map {
//            RecipeShort(
//                name = it.name,
//                pic = it.pic,
//                quantityIngredients = it.quantityIngredients
//            )
//        }.sortedBy { it.name })
    }

    fun load0(search: String = "") {
        isDataReady.value = false
        scope.launch {
//            if (lastIngredients != listIngredients || allRecipeFull.isEmpty()) {
            allRecipeFull.clear()
            if (search.isNotEmpty()) {
                all = RESTAPI.fetchRecipeFilteredFromText(search.replace("ё", "е"))
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


//                if (search.isNotEmpty() && listIngredients.isEmpty()) {
//                    all = RESTAPI.fetchRecipeFilteredFromText(search)
//                    allRecipeFull.addAll(all.map {
//                        RecipeFull(
//                            name = it.name,
//                            description = it.description,
//                            pic = it.pic,
//                            nutrition = it.nutrition,
//                            time = it.time,
//                            servings = it.servings,
//                            quantityIngredients = it.quantityIngredients,
//                            instructions = it.instructions
//                        )
//                    })
//                } else if (search.isNotEmpty()) {
//                    all = RESTAPI.fetchRecipeFilteredFromText(search)
//                    allRecipeFull.addAll(all.map {
//                        RecipeFull(
//                            name = it.name,
//                            description = it.description,
//                            pic = it.pic,
//                            nutrition = it.nutrition,
//                            time = it.time,
//                            servings = it.servings,
//                            quantityIngredients = it.quantityIngredients,
//                            instructions = it.instructions
//                        )
//                    })
//                } else {
//                    lastIndex = 0
//                    allRecipeFull.clear()
//                    all = RESTAPI.fetchRecipeFiltered(listIngredients)
//                    allRecipeFull.addAll(all.map {
//                        RecipeFull(
//                            name = it.name,
//                            description = it.description,
//                            pic = it.pic,
//                            nutrition = it.nutrition,
//                            time = it.time,
//                            servings = it.servings,
//                            quantityIngredients = it.quantityIngredients,
//                            instructions = it.instructions
//                        )
//                    })
//                }
//            lastIngredients = listIngredients
//            }
            isDataReady.value = true
        }
    }
}