package ru.cooksupteam.cooksup.model

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
)



