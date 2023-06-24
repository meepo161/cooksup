package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

data class RecipeShort(
    val name: String,
    val pic: String = "",
    val quantityIngredients: List<Measure> = listOf(),
    val ingredients: List<IngredientRemote> = quantityIngredients.map { it.ingredient },
)

@Serializable
data class RecipeShortRemote(
    val name: String,
    val pic: String = "",
    val quantityIngredients: List<Measure> = listOf(),
    val ingredients: List<IngredientRemote> = quantityIngredients.map { it.ingredient },
)
