package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.ui.components.IngredientListItem
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.viewmodel.IngredientsViewModel

class FridgeTab(var ivm: IngredientsViewModel) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.products)
            val icon = painterResource(id = R.drawable.fridge_icon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val listState = rememberLazyListState()
        CooksupTheme {
            Scaffold(
                modifier = Modifier.padding(bottom = 56.dp),
                backgroundColor = CooksupTheme.colors.uiBackground,
                topBar = {
                    TopAppBar(
                        elevation = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CooksupTheme.colors.uiBackground),
                        backgroundColor = Color.Transparent,
                        title = {
                            Text(
                                text = "Мои ингредиенты",
                                color = CooksupTheme.colors.brand
                            )
                        })
                }) {
                Row {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.background(CooksupTheme.colors.uiBackground),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "Всего ингредиентов: ${ivm.selectedIngredients.size}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                textAlign = TextAlign.Start,
                                color = CooksupTheme.colors.textPrimary
                            )
                        }
                        itemsIndexed(ivm.selectedIngredients) { index, ingredient ->
                            IngredientListItem(ingredient = ingredient, onClick = { _, _ ->
                                ivm.selectedIngredients.remove(ingredient)
                            })
                        }
                    }
                }
            }
        }
    }
}