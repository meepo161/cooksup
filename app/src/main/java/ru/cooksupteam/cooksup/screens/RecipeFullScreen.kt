package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.cooksupteam.cooksup.autoformat
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.toIntOrDefault
import ru.cooksupteam.cooksup.ui.components.RecipeImage
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

class RecipeFullScreen(var recipe: RecipeFull) : Screen {
    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState",
        "UnrememberedMutableState"
    )
    @Composable
    override fun Content() {
        var scrollState = rememberScrollState()
        val navigator = LocalNavigator.currentOrThrow
        val servingsState = remember { mutableStateOf(recipe.servings.toString()) }
        var servingsStateError = remember { mutableStateOf(false) }

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
                                text = recipe.name,
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
                            imageUrl = recipe.pic,
                            modifier = Modifier
                                .width(400.dp)
                                .height(400.dp),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier,
                                text = recipe.description,
                                color = CooksupTheme.colors.textPrimary
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row {
                                Text(
                                    text = "Калории:",
                                    color = CooksupTheme.colors.textPrimary
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color.LightGray)
                                        .align(Alignment.Bottom)
                                )
                                Text(
                                    text = (recipe.nutrition.calories / recipe.servings * servingsState.value.toIntOrDefault(
                                        0
                                    )).autoformat(),
                                    color = CooksupTheme.colors.textPrimary
                                )
                            }
                            Row {
                                Text(
                                    text = "Жиры:",
                                    color = CooksupTheme.colors.textPrimary
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color.LightGray)
                                        .align(Alignment.Bottom)
                                )
                                Text(
                                    text = (recipe.nutrition.fats / recipe.servings * servingsState.value.toIntOrDefault(
                                        0
                                    )).autoformat(),
                                    color = CooksupTheme.colors.textPrimary
                                )
                            }
                            Row {
                                Text(
                                    text = "Протеины:",
                                    color = CooksupTheme.colors.textPrimary
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color.LightGray)
                                        .align(Alignment.Bottom)
                                )
                                Text(
                                    text = (recipe.nutrition.proteins / recipe.servings * servingsState.value.toIntOrDefault(
                                        0
                                    )).autoformat(),
                                    color = CooksupTheme.colors.textPrimary
                                )
                            }
                            Row {
                                Text(
                                    text = "Белки:",
                                    color = CooksupTheme.colors.textPrimary
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color.LightGray)
                                        .align(Alignment.Bottom)
                                )
                                Text(
                                    text = (recipe.nutrition.carbohydrates / recipe.servings * servingsState.value.toIntOrDefault(
                                        0
                                    )).autoformat(),
                                    color = CooksupTheme.colors.textPrimary
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Время:",
                                color = CooksupTheme.colors.textPrimary
                            )
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(Color.LightGray)
                                    .align(Alignment.Bottom)
                            )
                            Text(
                                text = recipe.time,
                                color = CooksupTheme.colors.textPrimary
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp), verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "Количество порций:",
                                color = CooksupTheme.colors.textPrimary
                            )
                            Spacer(
                                modifier = Modifier
                                    .weight(0.9f)
                                    .height(1.dp)
                                    .background(Color.LightGray)
                                    .align(Alignment.Bottom)
                            )
                            BasicTextField(
                                modifier = Modifier.width(36.dp),
                                value = servingsState.value,
                                onValueChange = { servingsState.value = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(
                                    textAlign = TextAlign.Center,
                                    color = CooksupTheme.colors.brand
                                )
                            )
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
                            recipe.quantityIngredients.forEach {
                                Row {
                                    Text(
                                        text = it.ingredient.name,
                                        color = CooksupTheme.colors.textPrimary
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(1.dp)
                                            .background(Color.LightGray)
                                            .align(Alignment.Bottom)
                                    )
                                    Text(
                                        text = "${
                                            (it.amount / recipe.servings * servingsState.value.toIntOrDefault(
                                                0
                                            )).autoformat()
                                        } г.",
                                        color = CooksupTheme.colors.textPrimary
                                    )
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            recipe.instructions.forEachIndexed { i, step ->
                                Card(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .background(CooksupTheme.colors.uiBackground)
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
                                Log.d("MyLog", i.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}