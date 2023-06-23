package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.allIngredients
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.IngredientRemote

class IngredientsViewModel {
    var all = listOf<IngredientRemote>()
    var isDataReady = mutableStateOf(false)
    private val scope = CoroutineScope(Dispatchers.Default)

    fun load() {
        isDataReady.value = false
        scope.launch {
            all = RESTAPI.fetchIngredients()
            appendToAllIngredients()
            isDataReady.value = true
        }
    }

    private fun appendToAllIngredients() {
        allIngredients.clear()
        allIngredients.addAll(all.map {
            Ingredient(
                name = it.name.replace("процент", "%"),
                description = it.description,
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