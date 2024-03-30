package ru.cooksupteam.cooksup.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.ui.theme.Shapes

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp

@Composable
fun IngredientCard(
    ingredient: Ingredient,
    onSnackClick: (String) -> Unit,
    index: Int,
    gradient: List<Color>,
    gradientWidth: Float,
    scroll: Int,
    modifier: Modifier = Modifier
) {
    val left = index * with(LocalDensity.current) {
        (HighlightCardWidth + HighlightCardPadding).toPx()
    }
    CooksupCard(
        modifier = modifier,
        shape = Shapes.small
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = { onSnackClick(ingredient.name) })
                .height(128.dp)
                .fillMaxWidth()
        ) {
            val gradientOffset = left - (scroll / 3f)
            Row(
                modifier = Modifier
                    .height(128.dp)
                    .fillMaxWidth()
                    .offsetGradientBackground(gradient, gradientWidth, gradientOffset),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IngredientImage(
                    imageUrl = ingredient.pic,
                    modifier = Modifier
                        .size(140.dp)
                )
                Text(
                    text = ingredient.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6,
                    softWrap = true,
                    color = CooksupTheme.colors.textInteractive,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview("default")
@Preview(
    "dark theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun IngredientCardPreview() {
    CooksupTheme {
        IngredientCard(
            ingredient = Ingredient(
                "Карамельный попкорн",
                "Овощи и зелень",
                "https://foodcity.ru/storage/products/October2018/eP9jt5L6V510QjjT4a1B.jpg"
            ),
            onSnackClick = {},
            index = 1,
            gradient = CooksupTheme.colors.gradient2_1,
            gradientWidth = 380f,
            scroll = 10,
            modifier = Modifier
        )
    }
}
