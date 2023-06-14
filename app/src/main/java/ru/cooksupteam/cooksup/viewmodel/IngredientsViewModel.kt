package ru.cooksupteam.cooksup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.IngredientRemote

class IngredientsViewModel() {
    var currentPage = 0
    var all = listOf<IngredientRemote>()
    val allIngredients =
        mutableStateListOf(*all.map {
            Ingredient(
                name = it.name.replace("процент", "%"),
                group = it.group,
                image = "http://$ip:$port/ingredients_pics/" + it.name + ".png"
            )
        }.toTypedArray())

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        Log.d("DICK", "fetchIngridients")
        fetchIngredients()
    }

    fun fetchIngredients() {
        scope.launch {
            all = RESTAPI.fetchIngredients(currentPage)
            appendToAllIngredients()
            Log.d("DICK", all.toString())
        }
    }

    private fun appendToAllIngredients() {
        allIngredients.addAll(all.map {
            Ingredient(
                name = it.name.replace("процент", "%"),
                group = it.group,
                image = "http://$ip:$port/ingredients_pics/" + it.name + ".png",
            )
        })
    }

    val selectedIngredients = mutableStateListOf<Ingredient>()


    fun getFilteredIngredients(predicate: String): List<Ingredient> =
        allIngredients.filter { it.name.lowercase().contains(predicate.lowercase()) }

}