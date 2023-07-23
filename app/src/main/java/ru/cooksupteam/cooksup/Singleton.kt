package ru.cooksupteam.cooksup

import android.content.Context
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


object Singleton {
//     var ip = "192.168.1.78"
//     var ip = "192.168.0.17"
    var ip = "94.142.141.190"
    var port = "80"
    lateinit var appContext: Context
    val scope = CoroutineScope(Dispatchers.Default)
    lateinit var navigator: Navigator
}