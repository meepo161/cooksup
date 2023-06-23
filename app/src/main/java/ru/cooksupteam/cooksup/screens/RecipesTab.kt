package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.cooksupteam.cooksup.Singleton.allRecipeFull
import ru.cooksupteam.cooksup.Singleton.lastIndex
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.Singleton.selectedIngredients
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.ui.components.RecipeCard
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.viewmodel.RecipeFullViewModel

class RecipesTab() : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.recipes)
            val icon = rememberVectorPainter(Icons.Default.MenuBook)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
    @Composable
    override fun Content() {
        val navigatorTab = LocalNavigator.currentOrThrow
        val selectedIngredients = selectedIngredients.map { it.name }
        val rvmf = remember { RecipeFullViewModel(selectedIngredients) }
        val stateGrid = rememberLazyGridState(initialFirstVisibleItemIndex = lastIndex)
        val searchTextState = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

        LifecycleEffect(onStarted = {
            rvmf.load()
        })

        CooksupTheme {
            Scaffold(
                modifier = Modifier.padding(bottom = 56.dp),
                backgroundColor = CooksupTheme.colors.uiBackground,
                topBar = {
                    TopAppBar(
                        elevation = 0.dp,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = CooksupTheme.colors.uiBackground,
                        title = {
                            TextField(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Icon",
                                        tint = CooksupTheme.colors.brand,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Поиск рецептов",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp),
                                        textAlign = TextAlign.Start,
                                        fontSize = 20.sp,
                                        color = CooksupTheme.colors.textPrimary
                                    )
                                },
                                trailingIcon = {
                                    if (searchTextState.value !== "") {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close Icon",
                                            tint = CooksupTheme.colors.brand,
                                            modifier = Modifier
                                                .padding(horizontal = 12.dp)
                                                .clickable {
                                                    searchTextState.value = ""
                                                    rvmf.load()
                                                }
                                        )
                                    }
                                },
                                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
                                value = searchTextState.value,
                                onValueChange = {
                                    searchTextState.value = it
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        rvmf.load(searchTextState.value)
                                        keyboardController?.hide()
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CooksupTheme.colors.uiBackground),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = CooksupTheme.colors.uiBackground,
                                    cursorColor = CooksupTheme.colors.brand,
                                    focusedIndicatorColor = CooksupTheme.colors.brand,
                                    unfocusedIndicatorColor = CooksupTheme.colors.brand
                                )
                            )

                        })
                }) {
                if (rvmf.isDataReady.value) {
                    Column {
                        val size = allRecipeFull.filter {
                            it.name.lowercase().contains(searchTextState.value.lowercase())
                        }.size
                        Text(
                            text = "Найдено рецептов: ${if (size == 400) "400+" else size}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp, start = 12.dp),
                            textAlign = TextAlign.Start,
                            color = CooksupTheme.colors.textPrimary
                        )
                        LazyVerticalGrid(
                            state = stateGrid,
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .background(CooksupTheme.colors.uiBackground)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (stateGrid.isScrollInProgress) {
                                keyboardController?.hide()
                            }
                            itemsIndexed(allRecipeFull.filter {
                                it.name.lowercase().contains(searchTextState.value.lowercase())
                            }) { index, recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    onRecipeClick = {
                                        lastIndex = index
                                        navigator.push(RecipeFullScreen(recipe))
                                    },
                                    index = index,
                                    gradient = if (index % 2 == 0) CooksupTheme.colors.gradient6_1 else CooksupTheme.colors.gradient6_2,
                                    gradientWidth = 8000f,
                                    scroll = 1,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        )
                    ) {
                        CircularProgressIndicator(color = CooksupTheme.colors.brand)
                        Text(
                            text = "Идет поиск рецептов...",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = CooksupTheme.colors.brand
                        )
                    }
                }
            }
        }
    }
}