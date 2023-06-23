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
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.model.IngredientRemote
import ru.cooksupteam.cooksup.model.RecipeFullRemote
import ru.cooksupteam.cooksup.model.RecipesRemote
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader


object RESTAPI {
    var client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchIngredients(): List<IngredientRemote> {
        val file = appContext.resources.openRawResource(R.raw.ingredients_group)
        val ingredientsJson: Reader = BufferedReader(withContext(Dispatchers.IO) {
            InputStreamReader(file, "UTF8")
        })
        return Json.decodeFromString(string = ingredientsJson.readText())
    }

    suspend fun fetchAllRecipes(): List<RecipesRemote> {
        val response = client.get("http://$ip:$port/recipe_full1")
        return response.body()
    }

    suspend fun fetchRecipeFilteredFromText(name: String): List<RecipeFullRemote> {
        val response = client.get {
            url("http://$ip:$port/recipe_filtered_from_text")
            parameter("name", name)
        }
        return response.body()
    }

    suspend fun fetchRecipeFiltered(list: List<String>): List<RecipeFullRemote> {
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