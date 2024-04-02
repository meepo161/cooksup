package ru.cooksupteam.cooksup

import android.content.Context
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


object Singleton {
    lateinit var appContext: Context
    val scope = CoroutineScope(Dispatchers.Default)
    lateinit var navigator: Navigator
}