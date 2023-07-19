package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

data class RecipeFull(
    val name: String,
    val id: String = "",
    val description: String = "",
    val pic: String = "",
    val nutrition: Nutrition = Nutrition(),
    val time: String = "",
    val servings: Int = 1,
    val quantityIngredients: List<Measure> = listOf(),
    val ingredients: List<IngredientRemote> = quantityIngredients.map { it.ingredient },
    val instructions: List<Step> = listOf()

)

@Serializable
data class RecipeFullRemote(
    val name: String,
    val id: String = "",
    val description: String = "",
    val pic: String = "",
    val nutrition: Nutrition = Nutrition(),
    val time: String = "",
    val servings: Int = 1,
    val quantityIngredients: List<Measure> = listOf(),
    val ingredients: List<IngredientRemote> = quantityIngredients.map { it.ingredient },
    val instructions: List<Step> = listOf()
)
