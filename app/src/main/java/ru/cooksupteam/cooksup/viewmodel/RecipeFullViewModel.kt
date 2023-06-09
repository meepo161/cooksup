package ru.cooksupteam.cooksup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.runBlocking
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.model.RecipeFullRemote

class RecipeFullViewModel(listIngrediens: List<String>) {
    var all = listOf<RecipeFullRemote>()

    init {
        Log.d("DICK", "2")
        runBlocking {
            all = RESTAPI.fetchRecipeFull(listIngrediens)
        }
    }

    val allRecipeFull =
        mutableStateListOf(*all.map {
            RecipeFull(
                name = it.name,
                description = it.description,
                pic = "http://$ip:8080/recipes_pics/" + it.name + ".jpg",
                nutrition = it.nutrition,
                time = it.time,
                servings = it.servings,
                quantityIngredients = it.quantityIngredients,
                instructions = it.instructions
            )
        }.toTypedArray())
}