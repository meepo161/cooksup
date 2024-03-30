package ru.cooksupteam.cooksup.viewmodel

import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.Singleton.scope
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.app.rvm
import ru.cooksupteam.cooksup.model.Recipe
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader


class RecipeViewModel {
    lateinit var selectedRecipe: Recipe
    var isAllDataReady = mutableStateOf(false)
    var allRecipes = mutableStateListOf<Recipe>()
    var recipeFiltered = mutableStateListOf<Recipe>()
    private var favoriteRecipeRemote = listOf<Recipe>()
    var favoriteRecipe = mutableStateListOf<Recipe>()
    var lastIndexRecipe = 0
    var searchTextState = mutableStateOf("")
    var path = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS
    )

    @OptIn(ExperimentalSerializationApi::class)
    fun load(search: String = "") {
        isAllDataReady.value = false
        scope.launch {
            val file = File(path, "favorites.json")
            if (!file.exists()) {
                file.createNewFile()
            }
            if (allRecipes.isEmpty()) {
                for (i in 1..8) {
                    scope.launch {
                        Json.decodeFromStream<List<Recipe>>(
                            stream = appContext.assets.open("recipes$i.json")
                        ).asFlow().collect { recipe ->
                            rvm.allRecipes.add(recipe)
                        }
                        load()
                    }
                }
            }

            if (search.isNotEmpty()) {
                recipeFiltered.clear()
                recipeFiltered.addAll(
                    RESTAPI.fetchRecipeFilteredFromText(
                        search.replace("ё", "е").trim()
                    )
                )
            } else if (ivm.selectedIngredients.isNotEmpty()) {
                if (ivm.lastIngredients != ivm.selectedIngredients.map { it.name }) {
                    recipeFiltered.clear()
                    recipeFiltered.addAll(RESTAPI.fetchRecipeFiltered(ivm.selectedIngredients.map { it }))
                    ivm.lastIngredients = ivm.selectedIngredients.map { it.name }
                }
            }
            while (rvm.allRecipes.isEmpty()) {
                delay(10)
            }
            loadFavorite()
            isAllDataReady.value = true
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun loadFavorite() {
        favoriteRecipe.clear()
        val inputStream = File(path, "favorites.json").inputStream()
        try {
            favoriteRecipe.addAll(Json.decodeFromStream<List<Recipe>>(stream = inputStream))
        } catch (e: Exception) {
            Log.d("favoriteRecipe", e.toString())
        }
    }

    fun removeFavoriteRecipeFromJSON(recipe: Recipe) {
        val inputStream = File(path, "favorites.json").inputStream()
        val reader = BufferedReader(InputStreamReader(inputStream))
        val fileContent = reader.use { it.readText() }
        val gson = Gson()
        val allRecipes = if (fileContent.isNotEmpty()) {
            gson.fromJson(fileContent, Array<Recipe>::class.java).toMutableList()
        } else {
            emptyArray<Recipe>().toMutableList()
        }
        allRecipes.remove(recipe)
        val jsonString = gson.toJson(allRecipes)
        val outputStream = File(path, "favorites.json").outputStream()
        outputStream.write(jsonString.toByteArray())
        outputStream.close()
        Toast.makeText(
            appContext,
            "Рецепт удален из избранного",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun addFavoriteRecipeFromJSON(recipe: Recipe) {
        val inputStream = File(path, "favorites.json").inputStream()
        val reader = BufferedReader(InputStreamReader(inputStream))
        val fileContent = reader.use { it.readText() }
        val gson = Gson()
        val allRecipes = if (fileContent.isNotEmpty()) {
            gson.fromJson(fileContent, Array<Recipe>::class.java).toMutableList()
        } else {
            emptyArray<Recipe>().toMutableList()
        }
        allRecipes.add(recipe)
        val jsonString = gson.toJson(allRecipes)
        val outputStream = File(path, "favorites.json").outputStream()
        outputStream.write(jsonString.toByteArray())
        outputStream.close()
        Toast.makeText(
            appContext,
            "Рецепт добавлен в избранное",
            Toast.LENGTH_SHORT
        ).show()
    }

}