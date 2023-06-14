package ru.cooksupteam.cooksup

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.Singleton.isConnected
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.model.IngredientRemote
import ru.cooksupteam.cooksup.model.RecipeFullRemote
import ru.cooksupteam.cooksup.model.RecipesRemote
import java.io.File

object RESTAPI {
    var client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchIngredients(page: Int): List<IngredientRemote> {
        return if (isConnected()) {
            val file = File(appContext.filesDir, "ingredients_group.json")
            if (!file.exists()) {
                withContext(Dispatchers.IO) {
                    file.appendText(client.get("http://$ip:$port/ingredients?page=$page").body())
                }
            }
            Json.decodeFromString(string = file.readText())

        } else {
            List(0) { IngredientRemote("") }
        }
    }

    suspend fun fetchRecipes(): List<RecipesRemote> {
        val response = client.get("http://$ip:$port/recipe_full")
        return response.body()
    }

    suspend fun fetchRecipeFull(list: List<String>): List<RecipeFullRemote> {
        val response = client.get {
            url("http://$ip:$port/recipe_filtered")
            parameter("ingredients", list)
        }
        return response.body()
    }

    suspend fun postSelectedIngredients(list: List<IngredientRemote>) {
        client.post("http://$ip:$port/recipe_filtered") {
            contentType(ContentType.Application.Json)
            setBody(list)
        }
    }
}