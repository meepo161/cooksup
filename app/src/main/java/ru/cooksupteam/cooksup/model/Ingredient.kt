package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val name: String,
    val id: String? = "",
    val description: String = "",
    val pic: String = "",
    val nutrition: Nutrition = Nutrition(),
    val history: String = "",
    val benefitAndHarm: String = "",
    val taste: String = "",
    val howTo: String = "",
    val tags: List<String> = listOf(),
    val howLong: String = "",
    var selected: Boolean = false
)
