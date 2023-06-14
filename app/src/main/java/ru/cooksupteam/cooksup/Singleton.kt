package ru.cooksupteam.cooksup

import android.content.Context
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.IOException


object Singleton {
    var ip = "45.141.100.161"
    var port = "80"
    lateinit var appContext: Context
    val scope = CoroutineScope(Dispatchers.Default)
    lateinit var lastIngredients : List<String>

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