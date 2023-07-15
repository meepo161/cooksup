package ru.cooksupteam.cooksup.ui.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.cooksupteam.cooksup.model.RecipeFull
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp

@SuppressLint("UnrememberedMutableState")
@Composable
fun RecipeCard(
    recipe: RecipeFull,
    onRecipeClick: (String) -> Unit,
    index: Int,
    gradient: List<Color>,
    gradientWidth: Float,
    scroll: Int,
    modifier: Modifier = Modifier
) {
    val isSelected = mutableStateOf(recipe.favorite)
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
                .clickable(onClick = { onRecipeClick(recipe.name) })
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {
                val gradientOffset = left - (scroll / 3f)
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .offsetGradientBackground(gradient, gradientWidth, gradientOffset)
                )
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    Icon(
                        imageVector = if (isSelected.value) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        tint = if (isSelected.value) Color.Red else CooksupTheme.colors.uiBackground,
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .background(Color.Transparent)
                            .padding(4.dp)
                            .clickable {
                                isSelected.value = !isSelected.value
                                recipe.favorite = isSelected.value
                            }
                    )
                }
                RecipeImage(
                    imageUrl = recipe.pic,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.name,
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
@Preview(
    "dark theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SnackCardPreview() {
    CooksupTheme {
        Row {
            RecipeCard(
                recipe = RecipeFull(
                    "Рыбный салат",
                    "Рыбный салат быстрый,",
                    "https://foodcity.ru/storage/products/October2018/eP9jt5L6V510QjjT4a1B.jpg",
                ),
                onRecipeClick = {
                },
                index = 2,
                gradient = if (2 % 2 == 0) CooksupTheme.colors.gradient6_1 else CooksupTheme.colors.gradient6_2,
                gradientWidth = 1800f,
                scroll = 1,
                modifier = Modifier
            )
            RecipeCard(
                recipe = RecipeFull(
                    "Рыбный салат",
                    "Рыбный салат быстрый,",
                    "https://foodcity.ru/storage/products/October2018/eP9jt5L6V510QjjT4a1B.jpg",
                    favorite = true
                ),
                onRecipeClick = {
                },
                index = 2,
                gradient = if (1 % 2 == 0) CooksupTheme.colors.gradient6_1 else CooksupTheme.colors.gradient6_2,
                gradientWidth = 1800f,
                scroll = 1,
                modifier = Modifier
            )
        }
    }
}
