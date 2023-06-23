package ru.cooksupteam.cooksup

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.navigator.Navigator
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.RecipeFull
import java.io.IOException


object Singleton {
    var ip = "45.141.100.161"
//    var ip = "192.168.0.17"
    var port = "80"
    lateinit var appContext: Context
    val scope = CoroutineScope(Dispatchers.Default)
    var allRecipeFull = mutableStateListOf<RecipeFull>()
    var lastIngredients : List<String> = listOf()
    var allIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredients = mutableStateListOf<Ingredient>()
    var lastIndex = 0
    lateinit var navigator: Navigator

    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        var isCon: Boolean
        runBlocking {
            isCon = try {
                RESTAPI.client.get("http://$ip:$port/ingredients?page=0")
                true
            } catch (e: Exception) {
                false
            }
        }
        return isCon
    }
}