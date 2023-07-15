package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import ru.cooksupteam.cooksup.Singleton.lastIndexRecipe
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.Singleton.selectedIngredients
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.regex
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
        var recipeFullViewModel = remember { RecipeFullViewModel() }
        val searchTextState = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
//        val items = mutableStateListOf(*allRecipeShort.toTypedArray())

        LifecycleEffect(onStarted = {
            if (selectedIngredients.isNotEmpty() || searchTextState.value.isNotEmpty()) {
                recipeFullViewModel.load()
            } else {
                recipeFullViewModel.isDataReady.value = true
            }
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
                                                }
                                        )
                                    }
                                },
                                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
                                value = searchTextState.value,
                                onValueChange = {
                                    searchTextState.value =
                                        regex.replace(it, "").replace("Ё", "Е").replace("ё", "е")
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        if (selectedIngredients.isEmpty()) {
                                            recipeFullViewModel.load(searchTextState.value)
                                        }
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
                Column {
                    val items =
                        if (selectedIngredients.isNotEmpty() && searchTextState.value.isNotEmpty()) {
                            allRecipeFull.sortedBy { it.name.lowercase() }.filter {
                                it.name.lowercase()
                                    .contains(searchTextState.value.lowercase())
                            }
                        } else if (searchTextState.value.isNotEmpty()) {
                            allRecipeFull.sortedBy { it.name.lowercase() }.filter {
                                val nameRequest =
                                    searchTextState.value.trim().lowercase().split(' ')
                                        .toSet()
                                val nameRecipe = it.name.lowercase().split(' ').toSet()
                                if (nameRequest.size == 1) {
                                    if (searchTextState.value.length > 2) {
                                        it.name.trim().lowercase().contains(
                                            searchTextState.value.removeRange(
                                                3,
                                                searchTextState.value.length
                                            ).trim().lowercase()
                                        )
                                    } else {
                                        it.name.trim().lowercase()
                                            .contains(searchTextState.value.trim().lowercase())
                                    }
                                } else {
                                    (nameRequest subtract nameRecipe).isEmpty()
                                }
                            }
                        } else {
                            allRecipeFull
                        }
                    Text(
                        text = "Найдено рецептов: ${if (items.size == 400) "400+" else items.size}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp, start = 12.dp),
                        textAlign = TextAlign.Start,
                        color = CooksupTheme.colors.textPrimary
                    )
                    Column(modifier = Modifier.fillMaxSize()) {
                        val stateGrid = rememberLazyGridState(initialFirstVisibleItemIndex = 1)
                        LazyVerticalGrid(
                            state = stateGrid,
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .background(CooksupTheme.colors.uiBackground)
                                .fillMaxWidth()
                                .weight(0.8f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (stateGrid.isScrollInProgress) {
                                keyboardController?.hide()
                            }
                            itemsIndexed(items) { index, recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    onRecipeClick = {
                                        lastIndexRecipe = index
                                        navigator.push(RecipeFullScreen(recipe))
                                    },
                                    index = index,
                                    gradient = if (index % 2 == 0) CooksupTheme.colors.gradient6_1 else CooksupTheme.colors.gradient6_2,
                                    gradientWidth = 1800f,
                                    scroll = 1,
                                    modifier = if (index % 2 == 0) Modifier.padding(
                                        start = 12.dp,
                                        end = 4.dp
                                    ) else Modifier.padding(
                                        start = 4.dp, end = 12.dp
                                    )
                                )
                            }
                        }

                        AnimatedVisibility(!recipeFullViewModel.isDataReady.value) {
                            Box(
                                modifier = Modifier
                                    .weight(0.2f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = CooksupTheme.colors.brand)
                            }
                        }
                    }
                }
            }
        }
    }
}