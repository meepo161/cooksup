package ru.cooksupteam.cooksup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.model.Ingredient
import java.io.File

class IngredientsViewModel {
    private val scope = CoroutineScope(Dispatchers.Default)
    var isIngredientDataReady = mutableStateOf(true)
    var lastIngredients: List<String> = listOf()
    var allIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredientIdx = mutableStateOf<Int>(1)
    var searchTextState = mutableStateOf("")
    var items = mutableStateListOf<Ingredient>()
    val fileFields = File(Singleton.appContext.filesDir, "fields.json")

    init {
        load()
    }

    fun load() {
        if (allIngredients.isEmpty()) {
            scope.launch {
                isIngredientDataReady.value = false
                if (!fileFields.exists()) {
                    withContext(Dispatchers.IO) {
                        fileFields.createNewFile()
                    }
                }
                Log.d("fields", fileFields.readText())
                allIngredients.addAll(
                    Json.decodeFromStream<List<Ingredient>>(
                        stream = Singleton.appContext.assets.open(
                            "ingredients.json"
                        )
                    ).sortedBy { it.name }
                )
                items = mutableStateListOf(*allIngredients.toTypedArray())
                isIngredientDataReady.value = true
            }
        }
    }

    fun getFilteredIngredients(predicate: String): List<Ingredient> =
        allIngredients.filter { it.name.lowercase().contains(predicate.lowercase()) }
}