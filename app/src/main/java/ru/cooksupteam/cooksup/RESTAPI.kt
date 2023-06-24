package ru.cooksupteam.cooksup

import android.util.Log
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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.model.IngredientRemote
import ru.cooksupteam.cooksup.model.RecipeFullRemote
import ru.cooksupteam.cooksup.model.RecipeShortRemote
import java.io.File


object RESTAPI {
    var client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchIngredients(): List<IngredientRemote> {
        val file = File(appContext.cacheDir, "ingredients_group.json")
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

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun fetchAllRecipes(): List<RecipeShortRemote> {
        val file = File(appContext.cacheDir, "recipe_short.json")
        Log.d("FILE_EXISTS", file.exists().toString())
        if (!file.exists()) {
            withContext(Dispatchers.IO) {
                file.createNewFile()
                val response = client.get("http://$ip:$port/recipe_short")
                response.body<List<RecipeShortRemote>>().forEach {
                    file.appendText(
                        RecipeShortRemote(
                            it.name,
                            it.pic,
                            it.quantityIngredients,
                            it.ingredients
                        ).toString()
                    )
                }
            }
        }

//        return Json.decodeFromStream(fileInputStream)
        return listOf(RecipeShortRemote("хуйня"))
//        return Json.decodeFromString(string = file.readText())
//        val response = client.get("http://$ip:$port/recipe_short")
//        return response.body()
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