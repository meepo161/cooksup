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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
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
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.screens.IngredientDetailScreen

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
                width = 64.dp,
                height = 96.dp
            )
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = MaterialTheme.colors.primary),
                    onClick = {
                        ivm.selectedIngredientIdx.value = index + 1
                        navigator.push(IngredientDetailScreen(ingredient))
                    }
                )
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(48.dp)
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
                            .height(36.dp)
                            .fillMaxWidth()
                            .offsetGradientBackground(
                                listOf(
                                    MaterialTheme.colors.primary,
                                    MaterialTheme.colors.secondary
                                ),
                                gradientWidth,
                                gradientOffset
                            )
                            .clickable {
                                onClick(ingredient, isSelected.value)
                                isSelected.value = !isSelected.value
                                ingredient.selected = isSelected.value
                            }
                    )
                }
                IngredientImage(
                    imageUrl = ingredient.pic,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ingredient.name,
                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
//                softWrap = false,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxWidth()
            )
        }
    }
    Box(
        modifier = Modifier.size(12.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Icon(
            imageVector = if (isSelected.value) Icons.Rounded.Remove else Icons.Rounded.Add,
            tint = Color.Black,
            contentDescription = "",
            modifier = Modifier
                .background(
                    if (isSelected.value) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(6.dp)
                )
                .size(12.dp)
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun CardPreview() {
    MaterialTheme {
        IngredientListItem(
            ingredient = Ingredient(
                name = "Абрикосовое пюре",
                pic = "https://cdn.food.ru/unsigned/fit/640/480/ce/0/czM6Ly9tZWRpYS9waWN0dXJlcy9wcm9kdWN0cy83NjkvY292ZXJzL0QyV0xtZS5qcGc.jpg",
                selected = false
            ),
            index = 2,
            scroll = 1,
            gradient = listOf(MaterialTheme.colors.primary, MaterialTheme.colors.secondary),
            gradientWidth = 1800f
        ) { _, isSelected ->
        }
    }
}
