package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

@Serializable
data class Measure(val ingredient: IngredientRemote, val unit: String = "г", val amount: Double = 0.0, val toTaste: Boolean = false)
