package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.*
import kotlinx.serialization.Serializable
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

@Serializable
class MainScreen() :
    Screen {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        TabNavigator(MainTab()) {
            Scaffold(
                bottomBar = {
                    CooksupTheme {
                        BottomNavigation(
                            backgroundColor = CooksupTheme.colors.uiBackground,
                            contentColor = CooksupTheme.colors.textPrimary
                        ) {
                            TabNavigationItem(tab = MainTab())
                            TabNavigationItem(tab = SearchTab())
                            TabNavigationItem(tab = FridgeTab())
                            TabNavigationItem(tab = RecipesTab())
                        }
                    }
                }) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) },
        label = { Text(text = tab.options.title) }
    )
}

@Preview("default", showSystemUi = true, showBackground = true)
@Preview(
    "dark theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun MainScreenPreview() {
    MainScreen()
}
