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
import ru.cooksupteam.cooksup.model.IngredientRemote
import ru.cooksupteam.cooksup.model.RecipeFullRemote
import java.io.File


object RESTAPI {
    var client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchIngredients(): List<IngredientRemote> {
        val file = File(appContext.cacheDir, "ingredients.json")
        if (!file.exists()) {
            withContext(Dispatchers.IO) {
                file.createNewFile()
                val response = client.get("http://$ip:$port/ingredients")
                file.writeText(response.body())
            }
        }
//        val file = appContext.resources.openRawResource(R.raw.ingredients_group)
//        val ingredientsJson: Reader = BufferedReader(withContext(Dispatchers.IO) {
//            InputStreamReader(file, "UTF8")
//        })
        return Json.decodeFromString(string = file.readText())
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