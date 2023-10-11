package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

data class Field(
    val id: String? = "",
    val fields: List<String> = listOf(),
)

@Serializable
data class FieldRemote(
    val id: String? = "",
    val fields: List<String> = listOf()
)
