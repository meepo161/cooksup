package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import cafe.adriel.voyager.core.screen.Screen
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.ui.components.CooksupSurface
import ru.cooksupteam.cooksup.ui.components.IngredientImage
import ru.cooksupteam.cooksup.ui.components.SnackCard

import ru.cooksupteam.cooksup.utils.mirroringBackIcon
import kotlin.math.max
import kotlin.math.min


class IngredientDetailScreen(
    var ingredient: Ingredient, var onClickArrowBack: () -> Unit = { navigator.popUntilRoot() }
) : Screen {
    val BottomBarHeight = 56.dp
    val TitleHeight = 128.dp
    val GradientScroll = 180.dp
    val ImageOverlap = 115.dp
    val MinTitleOffset = 56.dp
    val MinImageOffset = 12.dp
    val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
    val ExpandedImageSize = 300.dp
    val CollapsedImageSize = 150.dp
    val HzPadding = Modifier.padding(horizontal = 24.dp)

    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter",
        "MutableCollectionMutableState",
        "UnrememberedMutableState"
    )
    @Composable
    override fun Content() {
        val textButton = mutableStateOf(
            if (ingredient.selected) {
                "Убрать из продуктов"
            } else {
                "Добавить в продукты"
            }
        )
        val snack = ingredient
        val related = ivm.allIngredients.filter {
            it.name.contains(
                ingredient.name.removeRange(
                    3, ingredient.name.length
                )
            )
        }.filter { it.name != ingredient.name }
        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        IconButton(
                            onClick = { onClickArrowBack() }, modifier = Modifier
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier
                            )
                        }
                        Text(
                            text = ingredient.name,

                            maxLines = 2, modifier = Modifier
                        )
                    })
                },
            ) {
                Box(Modifier.fillMaxSize()) {
                    val scroll = rememberScrollState(0)
                    Header()
                    Body(related, scroll)
                    Title(snack) { scroll.value }
                    Image(snack.pic) { scroll.value }
                    CartBottomBar(modifier = Modifier.align(Alignment.BottomCenter), textButton)
                }
            }
        }
    }

    @Composable
    fun Header() {
        Spacer(
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colors.primary, MaterialTheme.colors.secondary
                        )
                    )
                )
        )
    }

    @Composable
    private fun Body(
        related: List<Ingredient>, scroll: ScrollState
    ) {
        Column {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(MinTitleOffset)
            )
            Column(
                modifier = Modifier.verticalScroll(scroll)
            ) {
                Spacer(Modifier.height(GradientScroll))
                CooksupSurface(
                    Modifier.fillMaxWidth()

                ) {
                    Column(modifier = Modifier) {
                        Spacer(Modifier.height(ImageOverlap))
                        Spacer(Modifier.height(TitleHeight))
                        var seeMore by remember { mutableStateOf(true) }

                        Text(
                            text = "Описание",
                            style = MaterialTheme.typography.overline,
                            modifier = HzPadding.padding(vertical = 16.dp)
                        )
                        Text(
                            text = ingredient.description,
                            style = MaterialTheme.typography.caption,
                            maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            modifier = HzPadding.padding(vertical = 16.dp)
                        )
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = "История появления", style = MaterialTheme.typography.overline,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = ingredient.history,
                                style = MaterialTheme.typography.caption,
                                maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = "Польза и вред", style = MaterialTheme.typography.overline,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = ingredient.benefitAndHarm,
                                style = MaterialTheme.typography.caption,
                                maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = "На вкус", style = MaterialTheme.typography.overline,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = ingredient.taste,
                                style = MaterialTheme.typography.caption,
                                maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = "Как это есть/пить",
                                style = MaterialTheme.typography.overline,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = ingredient.howTo,
                                style = MaterialTheme.typography.caption,
                                maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = "Как и сколько хранить",
                                style = MaterialTheme.typography.overline,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }
                        AnimatedVisibility(visible = !seeMore) {
                            Text(
                                text = ingredient.howLong,
                                style = MaterialTheme.typography.caption,
                                maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis,
                                modifier = HzPadding.padding(vertical = 16.dp)
                            )
                        }

                        val textButton = if (seeMore) {
                            stringResource(id = R.string.see_more)
                        } else {
                            stringResource(id = R.string.see_less)
                        }
                        Text(text = textButton,
                            style = MaterialTheme.typography.button,
                            textAlign = TextAlign.Center,

                            modifier = Modifier
                                .heightIn(20.dp)
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                                .clickable {
                                    seeMore = !seeMore
                                })
                        Spacer(Modifier.height(40.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp, Alignment.CenterHorizontally
                            )
                        ) {
                            Box(
                                modifier = Modifier.weight(0.25f)

                            ) {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Калории",
                                        style = MaterialTheme.typography.button,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = ingredient.nutrition.calories.toString(),
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.weight(0.25f)

                            ) {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Белки",
                                        style = MaterialTheme.typography.button,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = ingredient.nutrition.proteins.toString(),
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.weight(0.25f)

                            ) {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Жиры",
                                        style = MaterialTheme.typography.button,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = ingredient.nutrition.fats.toString(),
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.weight(0.25f)

                            ) {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Углеводы",
                                        style = MaterialTheme.typography.button,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = ingredient.nutrition.carbohydrates.toString(),
                                        style = MaterialTheme.typography.button,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Divider(color = MaterialTheme.colors.primary)
                        Spacer(Modifier.height(16.dp))

                        LazyRow(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(related) { ingr ->
                                SnackCard(
                                    ingredient = ingr, onSnackClick = {
                                        navigator.push(
                                            IngredientDetailScreen(
                                                ingr
                                            )
                                        )
                                    }, index = 0, gradient = listOf(
                                        MaterialTheme.colors.primary, MaterialTheme.colors.secondary
                                    ), gradientWidth = 1800f, scroll = 1, modifier = Modifier
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .padding(bottom = BottomBarHeight)
                                .navigationBarsPadding()
                                .height(8.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun Title(snack: Ingredient, scrollProvider: () -> Int) {
        val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
        val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

        Column(verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .heightIn(min = TitleHeight)
                .statusBarsPadding()
                .offset {
                    val scroll = scrollProvider()
                    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                    IntOffset(x = 0, y = offset.toInt())
                }
                .background(color = MaterialTheme.colors.background)) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = snack.name,
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.secondary,
                modifier = HzPadding
            )
            Spacer(Modifier.height(8.dp))
            Divider(color = MaterialTheme.colors.primary)
        }
    }

    @Composable
    private fun Image(
        imageUrl: String, scrollProvider: () -> Int
    ) {
        val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
        val collapseFractionProvider = {
            (scrollProvider() / collapseRange).coerceIn(0f, 1f)
        }

        CollapsingImageLayout(
            collapseFractionProvider = collapseFractionProvider,
            modifier = HzPadding.then(Modifier.statusBarsPadding())
        ) {
            IngredientImage(
                imageUrl = imageUrl, modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    private fun CollapsingImageLayout(
        collapseFractionProvider: () -> Float,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Layout(
            modifier = modifier, content = content
        ) { measurables, constraints ->
            check(measurables.size == 1)

            val collapseFraction = collapseFractionProvider()

            val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
            val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
            val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
            val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

            val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
            val imageX = lerp(
                (constraints.maxWidth - imageWidth) / 2, // centered when expanded
                constraints.maxWidth - imageWidth, // right aligned when collapsed
                collapseFraction
            )
            layout(
                width = constraints.maxWidth, height = imageY + imageWidth
            ) {
                imagePlaceable.placeRelative(imageX, imageY)
            }
        }
    }

    @Composable
    private fun CartBottomBar(modifier: Modifier = Modifier, textButton: MutableState<String>) {
        val (count, updateCount) = remember { mutableStateOf(1) }
        CooksupSurface(modifier) {
            Column(Modifier) {
                Divider(color = MaterialTheme.colors.primary)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .then(HzPadding)
                        .heightIn(min = BottomBarHeight)
                ) {
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = {
                            if (ingredient.selected) {
                                ivm.selectedIngredients.remove(ingredient)
                                ingredient.selected = false
                                textButton.value = "Добавить в продукты"
                            } else {
                                ivm.selectedIngredients.add(ingredient)
                                ingredient.selected = true
                                textButton.value = "Убрать из продуктов"
                            }
                        }, modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = textButton.value,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}