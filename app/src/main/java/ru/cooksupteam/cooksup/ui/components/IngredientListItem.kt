package ru.cooksupteam.cooksup.ui.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Remove
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.app.ivm
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
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = CooksupTheme.colors.brand),
                    onClick = {
                        ivm.selectedIngredientIdx.value = index + 1
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
                AnimatedContent(isSelected.value, transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(300),
                        initialOffsetX = { if (isSelected.value) it else -it })
                        .with(
                            slideOutHorizontally(
                                animationSpec = tween(300),
                                targetOffsetX = { if (isSelected.value) -it else it })
                        )
                }) { isVisible ->
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .offsetGradientBackground(
                                if (isVisible) CooksupTheme.colors.gradient6_2 else CooksupTheme.colors.gradient6_1,
                                gradientWidth,
                                gradientOffset
                            )
                            .clickable {
                                onClick(ingredient, isSelected.value)
                                isSelected.value = !isSelected.value
                                ingredient.selected = isSelected.value
                            }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, end = 6.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Icon(
                            imageVector = if (isVisible) Icons.Rounded.Remove else Icons.Rounded.Add,
                            tint = Color.Black,
                            contentDescription = "",
                            modifier = Modifier
                                .background(
                                    if (isVisible) CooksupTheme.colors.brandSecondary else CooksupTheme.colors.brandSecondary,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(4.dp)
                                .size(16.dp)
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
