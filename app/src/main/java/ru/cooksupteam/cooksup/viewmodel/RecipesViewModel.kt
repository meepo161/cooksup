package ru.cooksupteam.cooksup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.runBlocking
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.model.Recipes
import ru.cooksupteam.cooksup.model.RecipesRemote

class RecipesViewModel {
    var all = listOf<RecipesRemote>()

    init {
        Log.d("DICK", "2")
        runBlocking {
            all = RESTAPI.fetchRecipes()
        }
    }

    val allRecipes =
        mutableStateListOf(*all.map {
            Recipes(
                name = it.name,
                image = "http://$ip:8080/recipes_pics/" + it.name + ".jpg",
                quantityIngredients = it.quantityIngredients
            )
        }.toTypedArray())
}