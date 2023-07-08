package ru.cooksupteam.cooksup.app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.screens.MainScreen
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.viewmodel.IngredientsViewModel
import java.io.File


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        appContext = applicationContext
        IngredientsViewModel().load()
        val file = File(appContext.filesDir, "id.txt")
        var id = ""
        if (!file.exists()) {
            file.createNewFile()
        }
        id = file.readText()
        if (id != "") {
            Singleton.scope.launch {
                Singleton.user = RESTAPI.fetchAuthPerson(id)
                Singleton.isAuthorized.value = true
            }
        }
        setContent {
            CooksupTheme {
                Navigator(MainScreen())
            }
        }
    }
}
