package ru.cooksupteam.cooksup.screens

import Banner
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import ru.cooksupteam.cooksup.app.yandexBannerAd
import ru.cooksupteam.cooksup.regex
import ru.cooksupteam.cooksup.ui.components.CompactIngredientCard
import ru.cooksupteam.cooksup.ui.components.IngredientListItem
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

class IngredientsTab : Tab {

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
    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState",
        "ResourceType"
    )
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
                Column() {
                    Text(
                        text = "Ингредиентов ${ivm.items.size}",
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.h6,
                        softWrap = false,
                        color = CooksupTheme.colors.textSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        yandexBannerAd.id = R.string.banner_ingredients_tab
                        item { Banner(yandexBannerAd.id) }
                        itemsIndexed(ivm.items.sortedBy { !it.selected }) { index, ingredient ->
                            CompactIngredientCard(
                                modifier = Modifier,
                                ingredient = ingredient
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