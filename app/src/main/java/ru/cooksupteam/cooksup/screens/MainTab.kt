package ru.cooksupteam.cooksup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.cooksupteam.cooksup.Singleton.allIngredients
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.model.Filter
import ru.cooksupteam.cooksup.ui.components.CooksupFilterChip
import ru.cooksupteam.cooksup.ui.components.SnackCard
import ru.cooksupteam.cooksup.ui.components.diagonalGradientBorder
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

class MainTab(var navigator: Navigator) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.main_tab)
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        var alphabet =
            allIngredients.map { it.name.first().uppercase() }.toSet().toList().toTypedArray()
        var scaffoldState = rememberScaffoldState()
        val scrollState = rememberLazyListState()

        CooksupTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .heightIn(max = 32.dp)
                            .background(CooksupTheme.colors.uiBackground)
                            .fillMaxWidth(),
                        title = {
                            Row(
                                modifier = Modifier
                                    .background(CooksupTheme.colors.uiBackground)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.moustache),
                                    contentDescription = stringResource(id = R.string.logo),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    color = CooksupTheme.colors.brand,
                                    fontSize = 22.sp
                                )
                            }
                        }, backgroundColor = CooksupTheme.colors.uiBackground
                    )
                },
            ) {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(CooksupTheme.colors.uiBackground),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                            contentPadding = PaddingValues(start = 12.dp, end = 8.dp),
                            modifier = Modifier
                                .heightIn(min = 56.dp)
                                .fillMaxWidth()
                        ) {
                            item {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Rounded.FilterList,
                                        tint = CooksupTheme.colors.brand,
                                        contentDescription = stringResource(R.string.label_filters),
                                        modifier = Modifier.diagonalGradientBorder(
                                            colors = CooksupTheme.colors.interactiveSecondary,
                                            shape = CircleShape
                                        )
                                    )
                                }
                            }
                            item {
                                CooksupFilterChip(Filter("Низкокалорийные"))
                            }
                            item {
                                CooksupFilterChip(Filter("Безлактозные"))
                            }
                        }
                    }
                    alphabet.forEachIndexed { _, alphabet ->
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = alphabet.toString(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.h6,
                                    softWrap = false,
                                    color = CooksupTheme.colors.textSecondary,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                LazyRow(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        Alignment.Start
                                    ),
                                    contentPadding = PaddingValues(
                                        start = 12.dp,
                                        end = 8.dp
                                    ),
                                    modifier = Modifier
                                        .heightIn(min = 56.dp)
                                        .fillMaxWidth()
                                ) {
                                    itemsIndexed(allIngredients.filter {
                                        it.name.startsWith(
                                            alphabet
                                        )
                                    }) { index, ingredient ->
                                        SnackCard(
                                            ingredient = ingredient,
                                            onSnackClick = {
                                                navigator.push(
                                                    IngredientDetailScreen(
                                                        ingredient,
                                                        navigator
                                                    )
                                                )
                                            },
                                            index = index,
                                            gradient = if (index % 2 == 0) CooksupTheme.colors.gradient6_2 else CooksupTheme.colors.gradient6_1,
                                            gradientWidth = 1800f,
                                            scroll = 1,
                                            modifier = Modifier
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