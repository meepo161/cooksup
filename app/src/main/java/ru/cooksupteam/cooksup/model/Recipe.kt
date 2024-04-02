package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val name: String,
    val id: String = "",
    val description: String = "",
    val pic: String = "",
    val nutrition: Nutrition = Nutrition(),
    val time: String = "",
    val servings: Int = 1,
    val quantityIngredients: List<Measure> = listOf(),
    val ingredients: List<Ingredient> = quantityIngredients.map { it.ingredient },
    val instructions: List<Step> = listOf()
)
