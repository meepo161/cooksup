package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.Singleton.allIngredients
import ru.cooksupteam.cooksup.Singleton.lastIndexIngredient
import ru.cooksupteam.cooksup.Singleton.pageRecipes
import ru.cooksupteam.cooksup.Singleton.searchTextStateStored
import ru.cooksupteam.cooksup.Singleton.selectedIngredients
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.regex
import ru.cooksupteam.cooksup.ui.components.IngredientListItem
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

class SearchTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.search_tab)
            val icon = rememberVectorPainter(Icons.Default.Search)

            return remember {
                TabOptions(
                    index = 1u,
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
        val listState = rememberLazyListState(initialFirstVisibleItemIndex = lastIndexIngredient)
        var isNeedSelectedHeader by remember { mutableStateOf(false) }
        val searchTextState = remember { mutableStateOf(searchTextStateStored) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val stateGrid =
            rememberLazyGridState(initialFirstVisibleItemIndex = Singleton.lastIndexRecipe)

        var items = mutableStateListOf(*allIngredients.toTypedArray())
        if (searchTextState.value != "") {
            items.clear()
            var toTypedArray = searchTextState.value.lowercase().split(" ")
                .toTypedArray().toMutableList()
            if (toTypedArray.last() == "") {
                toTypedArray.removeLast()
            }
            toTypedArray.forEach { s ->
                items.addAll(
                    allIngredients.toTypedArray()
                        .sortedBy { it.name.lowercase() }
                        .filter {
                            it.name.lowercase().contains(
                                s.trim().lowercase()
                            )
                        }
                )
            }
        }

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
                                        text = "Поиск ингредиентов",
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
                                    searchTextStateStored = searchTextState.value
                                    items.clear()

                                    var toTypedArray = searchTextState.value.lowercase().split(" ")
                                        .toTypedArray().toMutableList()
                                    if (toTypedArray.last() == "") {
                                        toTypedArray.removeLast()
                                    }
                                    toTypedArray.forEach { s ->
                                        items.addAll(
                                            allIngredients.toTypedArray()
                                                .sortedBy { it.name.lowercase() }
                                                .filter {
                                                    it.name.lowercase().contains(
                                                        s.trim().lowercase()
                                                    )
                                                }
                                        )
                                    }
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = { navigatorTab.push(RecipesTab()) }
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
                Row() {
                    Column {
                        Text(
                            text = "Всего ингредиентов: ${items.size}",
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
                            itemsIndexed(items.sortedBy { !it.selected }) { index, ingredient ->
                                if (listState.isScrollInProgress) {
                                    keyboardController?.hide()
                                }
                                IngredientListItem(
                                    ingredient = ingredient,
                                    index = index,
                                    gradient = if (ingredient.selected) CooksupTheme.colors.gradient6_2 else CooksupTheme.colors.gradient6_1,
                                    gradientWidth = 1800f,
                                    scroll = 1,
                                    modifier = if (index % 2 == 0) Modifier.padding(
                                        start = 12.dp,
                                        end = 4.dp
                                    ) else Modifier.padding(start = 4.dp, end = 12.dp),

                                    ) { _, isSelected ->
                                    if (!isSelected) {
                                        selectedIngredients.add(ingredient)
                                        pageRecipes = 1
                                    } else {
                                        selectedIngredients.remove(ingredient)
                                        pageRecipes = 1
                                    }
                                    isNeedSelectedHeader = false
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}