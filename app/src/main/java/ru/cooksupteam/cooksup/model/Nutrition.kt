package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

@Serializable
data class Nutrition(
    val calories: Double = 0.0,
    val proteins: Double = 0.0,
    val fats: Double = 0.0,
    val carbohydrates: Double = 0.0,
)