package ru.cooksupteam.cooksup

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.Singleton.ip
import ru.cooksupteam.cooksup.Singleton.port
import ru.cooksupteam.cooksup.app.rvm
import ru.cooksupteam.cooksup.model.FieldRemote
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.Person
import ru.cooksupteam.cooksup.model.Recipe


object RESTAPI {
    var client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 600000L
            connectTimeoutMillis = 600000L
            socketTimeoutMillis = 600000L
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    var ingredients =
        Json.decodeFromStream<List<Ingredient>>(stream = appContext.assets.open("ingredients.json"))

    suspend fun fetchVersionDB(): List<FieldRemote> {
        val response = client.get("http://$ip:$port/fields")
        return response.body()
    }

    fun fetchIngredients(): List<Ingredient> {
        return ingredients
    }

    fun fetchRecipeFilteredFromText(name: String): List<Recipe> {
        return rvm.allRecipes.filter {
            it.name.contains(name)
        }
    }

    fun fetchRecipeFiltered(ingredientsList: List<Ingredient>): List<Recipe> {
        val list = mutableListOf<Recipe>()
        val variations = generateVariations(ingredientsList)
        variations.forEach { variation ->
            rvm.allRecipes.filter { recipe ->
                recipe.ingredients.map(Ingredient::name).containsAll(variation.map { it.name })
            }.forEach {
                list.add(it)
            }
        }
        return list
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

    suspend fun postFavouriteRecipe(id: String, recipeId: String) {
        client.put("http://$ip:$port/favourite/$id") {
            contentType(ContentType.Application.Json)
            setBody(recipeId)
        }
    }

    suspend fun fetchFavoriteRecipes(id: String): List<Recipe> {
        val response = client.get {
            url("http://$ip:$port/favourite/$id")
        }
        return response.body()
    }

    fun generateVariations(ingredients: List<Ingredient>): List<List<Ingredient>> {
        val variations = mutableListOf<List<Ingredient>>()
        if (ingredients.isNotEmpty()) {
            for (i in ingredients.indices) {
                val variation = mutableListOf<Ingredient>()
                for (j in i until (i + ingredients.size)) {
                    val index = j % ingredients.size
                    variation.add(ingredients[index])
                    variations.add(variation.toList())
                }
            }
        }

        return variations.sortedBy { it.size }.reversed()
    }
}