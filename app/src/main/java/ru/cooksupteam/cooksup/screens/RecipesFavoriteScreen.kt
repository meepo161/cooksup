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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.Singleton.scope
import ru.cooksupteam.cooksup.app.rvm
import ru.cooksupteam.cooksup.app.uvm
import ru.cooksupteam.cooksup.regex
import ru.cooksupteam.cooksup.ui.components.CompactRecipeCard
import ru.cooksupteam.cooksup.ui.components.RecipeCard
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

class RecipesFavoriteScreen() : Screen {

    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
    @Composable
    override fun Content() {
        val navigatorTab = LocalNavigator.currentOrThrow
        val lazyListState: LazyListState =
            rememberLazyListState(initialFirstVisibleItemIndex = rvm.lastIndexRecipe)
        val searchTextState = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val scaffoldState = rememberScaffoldState()

        CooksupTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                snackbarHost = {
                    SnackbarHost(it) { data ->
                        Snackbar(
                            backgroundColor = CooksupTheme.colors.uiBackground,
                            contentColor = CooksupTheme.colors.brand,
                            snackbarData = data
                        )
                    }
                },
                backgroundColor = CooksupTheme.colors.uiBackground,
                topBar = {
                    TopAppBar(
                        elevation = 0.dp,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = CooksupTheme.colors.uiBackground,
                        title = {
                            IconButton(
                                onClick = { navigator.pop() },
                                modifier = Modifier.background(CooksupTheme.colors.uiBackground)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    tint = CooksupTheme.colors.brand,
                                    contentDescription = "Back",
                                    modifier = Modifier.background(CooksupTheme.colors.uiBackground)
                                )
                            }
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
                                        text = "Фильтр избранных",
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
                                    onSearch = { }
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

                if (!rvm.isAllDataReady.value) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically,
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = CooksupTheme.colors.brand,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            text = "Загрузка рецептов",
                            color = CooksupTheme.colors.brand
                        )
                    }
                } else if (rvm.favoriteRecipe.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CooksupTheme.colors.uiBackground)
                            .padding(horizontal = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                                .offset(y = -50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                tint = CooksupTheme.colors.brand,
                                modifier = Modifier.size(300.dp)
                            )
                            Text(
                                text = "Добавьте рецепты в избранное",
                                color = CooksupTheme.colors.brand,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Text(
                                text = "Выберите ингредиенты или введите название рецепта и добавляйте их",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                } else {
                    Column {
                        val items =
                            rvm.favoriteRecipe.sortedBy { it.name.lowercase() }.filter {
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

                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = if (!rvm.isAllDataReady.value) "" else if (items.size == 200) "Рецептов 200+" else "Рецептов ${items.size}",
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.h6,
                                softWrap = false,
                                color = CooksupTheme.colors.textSecondary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            LazyColumn(state = lazyListState) {
                                itemsIndexed(items) { index, recipe ->
                                    CompactRecipeCard(
                                        recipe = recipe,
                                        index = index
                                    ) { recipe, isFavorite ->
                                        if (!isFavorite) {
                                            rvm.addFavoriteRecipeFromJSON(recipe)
                                        } else {
                                            rvm.removeFavoriteRecipeFromJSON(recipe)
                                        }
                                    }
                                }
                            }

                            AnimatedVisibility(!rvm.isAllDataReady.value) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(
                                        16.dp,
                                        Alignment.CenterVertically,
                                    ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(
                                        color = CooksupTheme.colors.brand,
                                        modifier = Modifier.size(100.dp)
                                    )
                                    Text(
                                        text = "Загрузка рецептов",
                                        color = CooksupTheme.colors.brand
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}