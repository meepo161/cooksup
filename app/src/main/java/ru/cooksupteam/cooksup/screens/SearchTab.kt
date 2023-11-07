package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.app.ivm
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
        val keyboardController = LocalSoftwareKeyboardController.current
        val stateGrid =
            rememberLazyGridState(initialFirstVisibleItemIndex = ivm.selectedIngredientIdx.value)

        if (ivm.searchTextState.value != "") {
            ivm.items.clear()
            val toTypedArray = ivm.searchTextState.value.lowercase().split(" ")
                .toTypedArray().toMutableList()
            if (toTypedArray.last() == "") {
                toTypedArray.removeLast()
            }
            toTypedArray.forEach { s ->
                ivm.items.addAll(
                    ivm.allIngredients.toTypedArray()
                        .sortedBy { it.name.lowercase() }
                        .filter {
                            it.name.lowercase().contains(
                                s.trim().lowercase()
                            )
                        }
                )
            }
        } else {
            ivm.items.clear()
            ivm.items.addAll(ivm.allIngredients)
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
                                    if (ivm.searchTextState.value !== "") {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close Icon",
                                            tint = CooksupTheme.colors.brand,
                                            modifier = Modifier
                                                .padding(horizontal = 12.dp)
                                                .clickable {
                                                    ivm.searchTextState.value = ""
                                                }
                                        )
                                    }
                                },
                                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
                                value = ivm.searchTextState.value,
                                onValueChange = {
                                    ivm.searchTextState.value =
                                        regex.replace(it, "").replace("Ё", "Е").replace("ё", "е")
                                    ivm.items.clear()

                                    val toTypedArray =
                                        ivm.searchTextState.value.lowercase().split(" ")
                                            .toTypedArray().toMutableList()
                                    if (toTypedArray.last() == "") {
                                        toTypedArray.removeLast()
                                    }
                                    toTypedArray.forEach { s ->
                                        ivm.items.addAll(
                                            ivm.allIngredients.toTypedArray()
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
                Column {
                    Spacer(modifier = Modifier.size(8.dp))
                    LazyVerticalGrid(
                        state = stateGrid,
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .background(CooksupTheme.colors.uiBackground)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        item {
                            Text(
                                text = "Ингредиентов",
                                textAlign = TextAlign.End,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.h6,
                                softWrap = false,
                                color = CooksupTheme.colors.textSecondary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 4.dp
                                    )
                            )
                        }
                        item {
                            Text(
                                text = "${ivm.items.size}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.h6,
                                softWrap = false,
                                color = CooksupTheme.colors.textSecondary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 4.dp
                                    )
                            )
                        }
                        if (stateGrid.isScrollInProgress) {
                            keyboardController?.hide()
                        }
                        itemsIndexed(ivm.items.sortedBy { !it.selected }) { index, ingredient ->
                            if (stateGrid.isScrollInProgress) {
                                keyboardController?.hide()
                            }
                            IngredientListItem(
                                ingredient = ingredient,
                                index = index,
                                gradient = if (ingredient.selected) CooksupTheme.colors.gradient6_2 else CooksupTheme.colors.gradient6_1,
                                gradientWidth = 6000f,
                                scroll = 1,
                                modifier = if (index % 2 == 0) Modifier.padding(
                                    start = 12.dp,
                                    end = 4.dp
                                ) else Modifier.padding(start = 4.dp, end = 12.dp),

                                ) { _, isSelected ->
                                if (!isSelected) {
                                    ivm.selectedIngredients.add(ingredient)
                                } else {
                                    ivm.selectedIngredients.remove(ingredient)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}