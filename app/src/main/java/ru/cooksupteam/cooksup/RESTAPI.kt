package ru.cooksupteam.cooksup

import kotlinx.serialization.decodeFromString
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
import ru.cooksupteam.cooksup.app.ivm
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


    suspend fun fetchVersionDB(): List<FieldRemote> {
        val response = client.get("http://$ip:$port/fields")
        return response.body()
    }

    fun fetchRecipeFilteredFromText(name: String): List<Recipe> {
        return rvm.allRecipes.filter {
            it.name.contains(name)
        }
    }

    fun fetchRecipeFiltered(listIngredient: List<Ingredient>): List<Recipe> {
        val list = mutableListOf<Recipe>()
        val listRecipeOther = mutableListOf<Recipe>()
        val listRecipeOtherWithBase = mutableListOf<Recipe>()

        val listIngredientsWithBase = listIngredient.toMutableList()
        val listIngredientsWithBaseString = mutableListOf<String>()

        listIngredient.forEach {
            listIngredientsWithBaseString.add(it.name)
        }
        ivm.allIngredients.filter { it.tags.contains("база") }.forEach {
            listIngredientsWithBaseString.add(it.name)
        }

        filterRecipes(listIngredientsWithBaseString).forEach {
            list.add(it)
        }

        generateVariations(listIngredient).forEach { variation ->
            if (variation.size > listIngredient.size - 4) {
                rvm.allRecipes.filter { recipe ->
                    recipe.ingredients.map(Ingredient::name).containsAll(variation.map { it.name })
                }.forEach {
                    listRecipeOther.add(it)
                }
            }
        }

        generateVariations(listIngredientsWithBase).forEach { variation ->
            if (variation.size > listIngredientsWithBase.size - 4) {
                rvm.allRecipes.filter { recipe ->
                    recipe.ingredients.map(Ingredient::name).containsAll(variation.map { it.name })
                }.forEach {
                    listRecipeOtherWithBase.add(it)
                }
            }
        }

        val ingredientsListString = mutableListOf<String>()
        listIngredient.forEach {
            ingredientsListString.add(it.name)
        }
        list.addAll(sortRecipesBySimilarity(listRecipeOther, ingredientsListString))
        list.addAll(sortRecipesBySimilarity(listRecipeOtherWithBase, ingredientsListString))
        return sortRecipesBySimilarity(list, ingredientsListString).distinct()
    }

    fun similarity(selectedIngredients: List<String>, recipeIngredients: List<Ingredient>): Int {
        return selectedIngredients.intersect(recipeIngredients).count()
    }

    fun sortRecipesBySimilarity(
        recipes: List<Recipe>,
        selectedIngredients: List<String>
    ): List<Recipe> {
        return recipes.sortedByDescending { recipe ->
            similarity(selectedIngredients, recipe.ingredients)
        }
    }

    private fun filterRecipes(selectedIngredients: MutableList<String>): List<Recipe> {
        return rvm.allRecipes.filter { recipe ->
            recipe.ingredients.map { it.name }.containsAll(selectedIngredients)
        }
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

