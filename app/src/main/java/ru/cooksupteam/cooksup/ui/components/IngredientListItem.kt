package ru.cooksupteam.cooksup.ui.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.cooksupteam.cooksup.Singleton.lastIndexIngredient
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.screens.IngredientDetailScreen
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IngredientListItem(
    ingredient: Ingredient,
    index: Int,
    scroll: Int,
    gradient: List<Color>,
    gradientWidth: Float,
    modifier: Modifier = Modifier,
    onClick: (Ingredient, Boolean) -> Unit
) {
    val isSelected = mutableStateOf(ingredient.selected)
    val left = index * with(LocalDensity.current) {
        (HighlightCardWidth + HighlightCardPadding).toPx()
    }
    CooksupCard(
        modifier = modifier
            .size(
                width = 170.dp,
                height = 250.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = CooksupTheme.colors.brand),
                    onClick = {
                        lastIndexIngredient = index + 1
                        navigator.push(IngredientDetailScreen(ingredient))
                    }
                )
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {
                val gradientOffset = left - (scroll / 3f)
                AnimatedContent(targetState = isSelected.value) { targetState ->
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .offsetGradientBackground(
                                if (targetState) CooksupTheme.colors.gradient6_2 else CooksupTheme.colors.gradient6_1,
                                gradientWidth,
                                gradientOffset
                            )
                            .clickable {
                                onClick(ingredient, isSelected.value)
                                isSelected.value = !isSelected.value
                                ingredient.selected = isSelected.value
                            }
                    )
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = if (isSelected.value) Icons.Rounded.Remove else Icons.Rounded.Add,
                            tint = if (isSelected.value) CooksupTheme.colors.uiBackground else CooksupTheme.colors.brand,
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .background(Color.Transparent)
                                .padding(4.dp)
                        )
                    }
                }
                IngredientImage(
                    imageUrl = ingredient.pic,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ingredient.name,
                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
//                softWrap = false,
                color = CooksupTheme.colors.textSecondary,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun CardPreview() {
    CooksupTheme {
        IngredientListItem(
            ingredient = Ingredient(
                name = "Абрикосовое пюре",
                pic = "https://cdn.food.ru/unsigned/fit/640/480/ce/0/czM6Ly9tZWRpYS9waWN0dXJlcy9wcm9kdWN0cy83NjkvY292ZXJzL0QyV0xtZS5qcGc.jpg",
                selected = false
            ),
            index = 2,
            scroll = 1,
            gradient = CooksupTheme.colors.gradient6_2,
            gradientWidth = 1800f
        ) { _, isSelected ->
        }
    }
}
