package ru.cooksupteam.cooksup.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import ru.cooksupteam.cooksup.screens.MainScreen
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.viewmodel.IngredientsViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var ivm = IngredientsViewModel()
        setContent {
            CooksupTheme {
                Navigator(MainScreen(ivm))
            }
        }
    }
}
