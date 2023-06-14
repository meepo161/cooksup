package ru.cooksupteam.cooksup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.runBlocking
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.model.RecipeFull
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
            RecipeFull(
                name = it.name,
                pic = "http://$ip:$port/recipes_pics/" + it.name + ".jpg",
                quantityIngredients = it.quantityIngredients
            )
        }.toTypedArray())
}