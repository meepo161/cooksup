package ru.cooksupteam.cooksup.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.allIngredients
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.Singleton.isIngredientDataReady
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.IngredientRemote

class IngredientsViewModel {
    var all = listOf<IngredientRemote>()
    private val scope = CoroutineScope(Dispatchers.Default)

    fun load() {
        if (all.isEmpty()) {
            scope.launch {
                isIngredientDataReady.value = false
                all = RESTAPI.fetchIngredients()
                appendToAllIngredients()
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