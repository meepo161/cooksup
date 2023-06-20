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
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.model.IngredientRemote
import ru.cooksupteam.cooksup.model.RecipeFullRemote
import ru.cooksupteam.cooksup.model.RecipesRemote

object RESTAPI {
    var client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchIngredients(): List<IngredientRemote> {
//        val file = File(appContext.filesDir, "ingredients_group.json")
//        return if (!file.exists()) {
//            if (isConnected()) {
//                Log.d("FILE_EXISTS", "${file.exists()}")
//                withContext(Dispatchers.IO) {
//                    file.createNewFile()
//                    file.appendText(client.get("http://$ip:$port/ingredients?page=0").body())
//                }
//            }
//            Json.decodeFromString(string = file.readText())
//        } else {
//            Json.decodeFromString(string = file.readText())
//        }
        return client.get("http://$ip:$port/ingredients?page=0").body()
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