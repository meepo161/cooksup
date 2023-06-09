package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.ui.components.RecipeCard
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.viewmodel.IngredientsViewModel
import ru.cooksupteam.cooksup.viewmodel.RecipeFullViewModel

class RecipesTab(var ivm: IngredientsViewModel, var navigator: Navigator) : Tab {
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

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val selectedIngredients = ivm.selectedIngredients.map { it.name }
        val rvmf = RecipeFullViewModel(selectedIngredients)
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
                                text = "Список рецептов",
                                color = CooksupTheme.colors.brand
                            )
                        })
                }) {
                Column {
                    Text(
                        text = "Найдено рецептов: ${rvmf.allRecipeFull.size}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Start,
                        color = CooksupTheme.colors.textPrimary
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .background(CooksupTheme.colors.uiBackground)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(rvmf.allRecipeFull) { index, recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onRecipeClick = { navigator.push(RecipeFullScreen(recipe, ivm)) },
                                index = index,
                                gradient = if (index % 2 == 0) CooksupTheme.colors.gradient6_1 else CooksupTheme.colors.gradient6_2,
                                gradientWidth = 8000f,
                                scroll = 1,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}