package ru.cooksupteam.cooksup.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

class Ingredient(
    val name: String,
    val description: String = "",
    val pic: String = "",
    val nutrition: Nutrition = Nutrition(),
    val history: String = "",
    val benefitAndHarm: String = "",
    val taste: String = "",
    val howTo: String = "",
    val howLong: String = "",
    var selected: Boolean = false
)

@Serializable
data class IngredientRemote(
    val name: String,
    val description: String = "",
    val pic: String = "",
    val nutrition: Nutrition = Nutrition(),
    val history: String = "",
    val benefitAndHarm: String = "",
    val taste: String = "",
    val howTo: String = "",
    val howLong: String = ""
)
