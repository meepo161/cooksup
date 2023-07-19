package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val favourite: List<String> = listOf()
)