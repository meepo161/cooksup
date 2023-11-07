package ru.cooksupteam.cooksup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.model.Field
import ru.cooksupteam.cooksup.model.FieldRemote
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.IngredientRemote
import java.io.File

class IngredientsViewModel {
    var all = listOf<IngredientRemote>()
    private val scope = CoroutineScope(Dispatchers.Default)
    var isIngredientDataReady = mutableStateOf(true)
    var lastIngredients: List<String> = listOf()
    var allIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredientIdx = mutableStateOf<Int>(1)
    var searchTextState = mutableStateOf("")
    var items = mutableStateListOf<Ingredient>()
    var fieldsRemote = listOf<FieldRemote>()
    var fieldObject = Field()
    val fileFields = File(Singleton.appContext.filesDir, "fields.json")

    init {
        load()
    }

    fun load() {
        if (all.isEmpty()) {
            scope.launch {
                isIngredientDataReady.value = false
                if (!fileFields.exists()) {
                    withContext(Dispatchers.IO) {
                        fileFields.createNewFile()
                    }
                }
                fieldsRemote = RESTAPI.fetchVersionDB()
                fieldObject = Field(fields = fieldsRemote[0].fields)
                Log.d("fields", fieldObject.fields[0])
                Log.d("fields", fileFields.readText())
                all = RESTAPI.fetchIngredients()
                appendToAllIngredients()
                items = mutableStateListOf(*allIngredients.toTypedArray())
                isIngredientDataReady.value = true
                fileFields.writeText(fieldObject.fields[0])
            }
        }
    }

    private fun appendToAllIngredients() {
        allIngredients.clear()
        allIngredients.addAll(all.map {
            Ingredient(
                name = it.name.replace("процент", "%"),
                description = it.description,
//                pic = "http://$ip:$port/ingredients_pics/" + it.name + ".webp",
                pic = it.pic,
                nutrition = it.nutrition,
                history = it.history,
                benefitAndHarm = it.benefitAndHarm,
                taste = it.taste,
                howTo = it.howTo,
                howLong = it.howLong,
                tags = it.tags,
            )
        }.sortedBy { it.name })
    }

    fun getFilteredIngredients(predicate: String): List<Ingredient> =
        allIngredients.filter { it.name.lowercase().contains(predicate.lowercase()) }
}