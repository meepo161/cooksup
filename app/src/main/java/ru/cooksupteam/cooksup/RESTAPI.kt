package ru.cooksupteam.cooksup

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
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
import ru.cooksupteam.cooksup.Singleton.isJsonReady
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.model.IngredientRemote
import ru.cooksupteam.cooksup.model.Person
import ru.cooksupteam.cooksup.model.RecipeFullRemote
import java.io.File


object RESTAPI {
    var client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000L
            connectTimeoutMillis = 60000L
            socketTimeoutMillis = 60000L
        }
    }

    suspend fun fetchIngredients(): List<IngredientRemote> {
//        val file = File(appContext.filesDir, "ingredients.json")
//        if (!file.exists()) {
//            withContext(Dispatchers.IO) {
//                isJsonReady.value = false
//                file.createNewFile()
//                val response = client.get("http://$ip:$port/ingredients")
//                file.writeText(response.body())
//            }
//        }
//
//        isJsonReady.value = true
//        return Json.decodeFromString(string = file.readText())

        val response = client.get("http://$ip:$port/ingredients")
        Log.d("1111", response.body<List<IngredientRemote>>().toString())
        return response.body<List<IngredientRemote>>()
    }

    suspend fun fetchRecipeFilteredFromText(name: String): List<RecipeFullRemote> {
        val response = client.get {
            url("http://$ip:$port/recipe_filtered_from_text/$name")
        }
        return response.body()
    }

    suspend fun fetchRecipeFiltered(list: List<String>): List<RecipeFullRemote> {
        val response = client.get {
            url("http://$ip:$port/recipe_filtered/$list")
        }
        return response.body()
    }

    suspend fun postPerson(person: Person) {
        client.post("http://$ip:$port/person") {
            contentType(ContentType.Application.Json)
            setBody(person)
        }
    }

    suspend fun fetchPerson(list: List<String>): Person {
        val response = client.get {
            url("http://$ip:$port/person/$list")
        }
        return response.body()
    }

    suspend fun fetchAuthPerson(id: String): Person {
        val response = client.get {
            url("http://$ip:$port/person/auth/$id")
        }
        return response.body()
    }

    suspend fun fetchAllPerson(email: String): Person {
        val response = client.get {
            url("http://$ip:$port/person/$email")
        }
        return response.body()
    }
}