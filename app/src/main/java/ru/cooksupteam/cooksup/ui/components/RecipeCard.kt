package ru.cooksupteam.cooksup.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import ru.cooksupteam.cooksup.model.RecipeShort
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp

@Composable
fun RecipeCard(
    recipe: RecipeShort,
    onRecipeClick: (String) -> Unit,
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
                .clickable(onClick = { onRecipeClick(recipe.name) })
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .width(170.dp)
            ) {
                val gradientOffset = left - (scroll / 3f)
                Box(
                    modifier = Modifier
                        .height(64.dp)
                        .width(170.dp)
                        .offsetGradientBackground(gradient, gradientWidth, gradientOffset)
                )
                RecipeImage(
                    imageUrl = recipe.pic,
                    modifier = Modifier
                        .size(140.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.name,
                softWrap = true,
                maxLines = 3,
                fontSize = 14.sp,
                color = CooksupTheme.colors.textHelp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize()
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
//    CooksupTheme {
//        RecipeCard(
//            recipe = RecipeFull(
//                "Рыбный салат с авокадо и творожным сыром Рыбный салат с авокадо и творожным сыром",
//                "Рыбный салат с авокадо и творожным сыром обязательно понравится любителям суши! Простой и быстрый, а значит, идеален для современного человека. Рестораны делают на такой салат пятикратную наценку, но любителям давно известно, что рецепты можно повторить дома без особых усилий!Знайте: c каждым кусочком вы будете все больше переносится в ощущение отпуска. Морской прибой, мягкий теплый ветер, что обволакивает плечи и гастрономическое удовольствие, сила которого не сравнима с усилиями от готовки",
//                "https://foodcity.ru/storage/products/October2018/eP9jt5L6V510QjjT4a1B.jpg",
//                Nutrition(1.0, 2.0, 3.0, 4.0)
//            ),
//            onRecipeClick = {},
//            index = 2,
//            gradient = CooksupTheme.colors.gradient2_1,
//            gradientWidth = 380f,
//            scroll = 1,
//            modifier = Modifier.width(800.dp)
//        )
//    }
}
