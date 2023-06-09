package ru.cooksupteam.cooksup

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.model.IngredientRemote
import ru.cooksupteam.cooksup.model.RecipeFullRemote
import ru.cooksupteam.cooksup.model.RecipesRemote

object RESTAPI {
    var client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchIngredients(page: Int): List<IngredientRemote> {
        val response = client.get("http://$ip:8080/ingredients?page=$page")
        return response.body()
    }

    suspend fun fetchRecipes(): List<RecipesRemote> {
        val response = client.get("http://$ip:8080/recipes")
        return response.body()
    }

    suspend fun fetchRecipeFull(list: List<String>): List<RecipeFullRemote> {
        val response = client.get {
            url("http://$ip:8080/recipe_filtered")
            parameter("ingredients", list)
        }
        return response.body()
    }

    suspend fun postSelectedIngredients(list: List<IngredientRemote>) {
        client.post("http://$ip:8080/recipe_filtered") {
            contentType(ContentType.Application.Json)
            setBody(list)
        }
    }
}