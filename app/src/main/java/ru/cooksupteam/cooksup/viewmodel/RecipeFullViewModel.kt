package ru.cooksupteam.cooksup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.model.RecipeFullRemote

class RecipeFullViewModel(listIngrediens: List<String>) {
    var all = listOf<RecipeFullRemote>()
    var allRecipeFull = mutableStateListOf<RecipeFull>()

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        Log.d("DICK", "2")
        scope.launch {
            all = RESTAPI.fetchRecipeFull(listIngrediens)
            allRecipeFull.clear()
            allRecipeFull.addAll(all.map {
                RecipeFull(
                    name = it.name,
                    description = it.description,
                    pic = "http://$ip:$port/recipes_pics/" + it.name + ".jpg",
                    nutrition = it.nutrition,
                    time = it.time,
                    servings = it.servings,
                    quantityIngredients = it.quantityIngredients,
                    instructions = it.instructions
                )
            })
        }
    }

}