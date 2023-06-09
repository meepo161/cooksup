package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

data class Recipes(
    val name: String,
//    val description: String = "",
    val image: String = "",
//    val nutrition: Nutrition = Nutrition(),
//    val time: String = "",
//    val servings: Int = 1,
    val quantityIngredients: List<Measure> = listOf(),
    val ingredients: List<IngredientRemote> = quantityIngredients.map { it.ingredient },
//    val instructions: List<Step> = listOf()
)

@Serializable
data class RecipesRemote(val name: String, val quantityIngredients: List<Measure>)
