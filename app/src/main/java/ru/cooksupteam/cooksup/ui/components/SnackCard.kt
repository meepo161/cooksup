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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp

@Composable
fun SnackCard(
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
        modifier = modifier
            .size(
                width = 170.dp,
                height = 250.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = { onSnackClick(ingredient.name) })
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
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
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
        SnackCard(
            ingredient = Ingredient(
                "Огурец",
                "Овощи и зелень",
                "https://foodcity.ru/storage/products/October2018/eP9jt5L6V510QjjT4a1B.jpg"
            ),
            onSnackClick = {},
            index = 2,
            gradient = CooksupTheme.colors.gradient2_1,
            gradientWidth = 380f,
            scroll = 1,
            modifier = Modifier
            )
    }
}
