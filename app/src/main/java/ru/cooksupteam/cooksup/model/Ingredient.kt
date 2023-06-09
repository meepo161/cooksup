package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

class Ingredient(
    var name: String,
    var group: String,
    var image: String,
    var selected: Boolean = false
)

@Serializable
data class IngredientRemote(val name: String, val group: String = "")
