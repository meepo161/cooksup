package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.IngredientRemote

class IngredientsViewModel {
    var all = listOf<IngredientRemote>()
    private val scope = CoroutineScope(Dispatchers.Default)
    var isIngredientDataReady = mutableStateOf(true)
    var lastIngredients: List<String> = listOf()
    var allIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredientIdx = mutableStateOf<Int>(1)
    var searchTextStateStored = ""
    var items = mutableStateListOf<Ingredient>()

    init {
        load()
    }

    fun load() {
        if (all.isEmpty()) {
            scope.launch {
                isIngredientDataReady.value = false
                all = RESTAPI.fetchIngredients()
                appendToAllIngredients()
                items = mutableStateListOf(*ivm.allIngredients.toTypedArray())
                isIngredientDataReady.value = true
            }
        }
    }

    private fun appendToAllIngredients() {
        allIngredients.clear()
        allIngredients.addAll(all.map {
            Ingredient(
                name = it.name.replace("процент", "%"),
                description = it.description,
//                pic = "http://$ip:$port/ingredients_pics/" + it.name + ".webp",
                pic = it.pic,
                nutrition = it.nutrition,
                history = it.history,
                benefitAndHarm = it.benefitAndHarm,
                taste = it.taste,
                howTo = it.howTo,
                howLong = it.howLong,
            )
        }.sortedBy { it.name })
    }

    fun getFilteredIngredients(predicate: String): List<Ingredient> =
        allIngredients.filter { it.name.lowercase().contains(predicate.lowercase()) }
}