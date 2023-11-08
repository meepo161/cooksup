package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.app.rvm
import ru.cooksupteam.cooksup.autoformat
import ru.cooksupteam.cooksup.model.Recipe
import ru.cooksupteam.cooksup.toIntOrDefault
import ru.cooksupteam.cooksup.ui.components.IngredientImage
import ru.cooksupteam.cooksup.ui.components.RecipeImage
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

class RecipeScreen() : Screen {
    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState",
        "UnrememberedMutableState"
    )
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val servingsState = remember { mutableStateOf(rvm.selectedRecipe.servings.toString()) }
        CooksupTheme {
            Scaffold(
                backgroundColor = CooksupTheme.colors.uiBackground,
                topBar = {
                    TopAppBar(
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
                            Text(
                                text = rvm.selectedRecipe.name,
                                color = CooksupTheme.colors.brand,
                                maxLines = 2,
                                modifier = Modifier.background(CooksupTheme.colors.uiBackground)
                            )
                        }
                    )
                },
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CooksupTheme.colors.uiBackground)
                        .padding(8.dp),
                ) {
                    item {
                        RecipeImage(
                            imageUrl = rvm.selectedRecipe.pic,
                            modifier = Modifier
                                .width(400.dp)
                                .height(400.dp),
                            shape = RectangleShape
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier,
                                text = rvm.selectedRecipe.description,
                                color = CooksupTheme.colors.textPrimary
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(0.25f)
                                    .background(CooksupTheme.colors.uiBackground)
                            ) {
                                Column(
                                    modifier = Modifier.background(CooksupTheme.colors.uiBackground),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Калории",
                                        style = MaterialTheme.typography.button,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                    Text(
                                        text = rvm.selectedRecipe.nutrition.calories.toString(),
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(0.25f)
                                    .background(CooksupTheme.colors.uiBackground)
                            ) {
                                Column(
                                    modifier = Modifier.background(CooksupTheme.colors.uiBackground),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Белки",
                                        style = MaterialTheme.typography.button,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                    Text(
                                        text = rvm.selectedRecipe.nutrition.proteins.toString(),
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(0.25f)
                                    .background(CooksupTheme.colors.uiBackground)
                            ) {
                                Column(
                                    modifier = Modifier.background(CooksupTheme.colors.uiBackground),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Жиры",
                                        style = MaterialTheme.typography.button,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                    Text(
                                        text = rvm.selectedRecipe.nutrition.fats.toString(),
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(0.25f)
                                    .background(CooksupTheme.colors.uiBackground)
                            ) {
                                Column(
                                    modifier = Modifier.background(CooksupTheme.colors.uiBackground),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Углеводы",
                                        style = MaterialTheme.typography.button,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                    Text(
                                        text = rvm.selectedRecipe.nutrition.carbohydrates.toString(),
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Box(
                            modifier = Modifier.background(CooksupTheme.colors.uiBackground)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(CooksupTheme.colors.uiBackground)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Время приготовления",
                                    style = MaterialTheme.typography.button,
                                    textAlign = TextAlign.Center,
                                    color = CooksupTheme.colors.textLink
                                )
                                Text(
                                    text = rvm.selectedRecipe.time,
                                    style = MaterialTheme.typography.button,
                                    textAlign = TextAlign.Center,
                                    color = CooksupTheme.colors.textLink
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Box(
                            modifier = Modifier.background(CooksupTheme.colors.uiBackground)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    servingsState.value =
                                        (servingsState.value.toIntOrDefault(0) - 1).toString()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Remove,
                                        contentDescription = "",
                                        tint = CooksupTheme.colors.brand
                                    )
                                }
                                Column(
                                    modifier = Modifier.background(CooksupTheme.colors.uiBackground),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Количество порций",
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center,
                                        color = CooksupTheme.colors.textLink
                                    )
                                    BasicTextField(
                                        modifier = Modifier.width(36.dp),
                                        value = servingsState.value,
                                        onValueChange = { servingsState.value = it },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        textStyle = LocalTextStyle.current.copy(
                                            textAlign = TextAlign.Center,
                                            color = CooksupTheme.colors.textLink
                                        )
                                    )
                                }
                                IconButton(onClick = {
                                    servingsState.value =
                                        (servingsState.value.toIntOrDefault(0) + 1).toString()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "",
                                        tint = CooksupTheme.colors.brand
                                    )
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Box {
                                Text(
                                    text = "Используемые ингредиенты:",
                                    color = CooksupTheme.colors.textPrimary
                                )
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            rvm.selectedRecipe.quantityIngredients.forEach { measure ->
                                val ingredient =
                                    ivm.allIngredients.find { it.name == measure.ingredient.name }!!
                                Column {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.clickable {
                                            Singleton.navigator.push(
                                                IngredientDetailScreen(
                                                    ingredient
                                                ) {
                                                    navigator.pop()
                                                }
                                            )
                                        },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IngredientImage(
                                            imageUrl = ingredient.pic,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Text(
                                            text = measure.ingredient.name,
                                            color = CooksupTheme.colors.textPrimary
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(1.dp)
                                                .background(Color.Transparent)
                                                .align(Alignment.Bottom)
                                        )
                                        Text(
                                            text = "${
                                                (measure.amount / rvm.selectedRecipe.servings * servingsState.value.toIntOrDefault(
                                                    0
                                                )).autoformat()
                                            } г.",
                                            color = CooksupTheme.colors.textPrimary
                                        )
                                    }
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rvm.selectedRecipe.instructions.forEachIndexed { i, step ->
                                Card(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .background(CooksupTheme.colors.uiBackground)
                                        .fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier.background(CooksupTheme.colors.uiBackground)) {
                                        Column(modifier = Modifier.background(CooksupTheme.colors.uiBackground)) {
                                            if (step.pic.isNotEmpty()) {
                                                RecipeImage(
                                                    imageUrl = step.pic,
                                                    modifier = Modifier
                                                        .width(400.dp)
                                                        .height(400.dp),
                                                    shape = RoundedCornerShape(0.dp)
                                                )
                                            }
                                            Text(
                                                modifier = Modifier.padding(16.dp),
                                                text = "Шаг ${i + 1}: \n${step.text}",
                                                color = CooksupTheme.colors.textPrimary
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
    }
}