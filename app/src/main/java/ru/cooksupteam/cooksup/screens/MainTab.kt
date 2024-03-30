package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToJsonElement
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.Singleton.scope
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.app.firstStart
import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.app.rvm
import ru.cooksupteam.cooksup.model.Recipe
import ru.cooksupteam.cooksup.ui.components.SnackCard

import java.io.File


class MainTab : Tab {
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

    @OptIn(ExperimentalSerializationApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val scaffoldState = rememberScaffoldState()
        val scrollState = rememberLazyListState()

        LifecycleEffect(onStarted = {
            scope.launch {
                if (firstStart) {
                    Log.d("firstStart", "22222222222222")
                    firstStart = false
                    rvm.allRecipes.clear()
                    Json.decodeFromStream<List<Recipe>>(
                        stream = Singleton.appContext.assets.open(
                            "recipes0.json"
                        )
                    ).asFlow().collect { recipe ->
                        rvm.allRecipes.add(recipe)
                    }
                    scope.launch {
                        for (i in 1..8){
                            Json.decodeFromStream<List<Recipe>>(
                                stream = Singleton.appContext.assets.open(
                                    "recipes$i.json"
                                )
                            ).asFlow().collect { recipe ->
                                rvm.allRecipes.add(recipe)
                            }
                        }
                    }
//                    rvm.allRecipes.addAll(
//                        Json.decodeFromStream<List<Recipe>>(
//                            stream = Singleton.appContext.assets.open(
//                                "recipes.json"
//                            )
//                        )
//                    )
                }
            }
        })

        MaterialTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background,
                topBar = {
                    TopAppBar(
                        elevation = 0.dp,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.background,
                        title = {
                            Row(
                                modifier = Modifier
                                    .background(MaterialTheme.colors.background),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo2),
                                    contentDescription = stringResource(id = R.string.logo),
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    color = MaterialTheme.colors.primary,
                                    fontSize = 22.sp
                                )
                            }
                        }
                    )
                },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier
                            .padding(bottom = 56.dp, top = 8.dp)
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                repeat(5) { repeat ->
                                    var text = ""
                                    var tag = ""
                                    when (repeat) {
                                        0 -> {
                                            text = "Фрукты"
                                            tag = "фрукт"
                                        }

                                        1 -> {
                                            text = "Овощи"
                                            tag = "овощ"
                                        }

                                        2 -> {
                                            text = "Ягоды"
                                            tag = "ягода"
                                        }

                                        3 -> {
                                            text = "Мясо"
                                            tag = "мясо"
                                        }

                                        4 -> {
                                            text = "Морепродукты"
                                            tag = "морепродукты"
                                        }
                                    }
                                    Text(
                                        text = text,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.h6,
                                        softWrap = false,
                                        color = MaterialTheme.colors.secondary,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    LazyRow(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.Start
                                        ),
                                        contentPadding = PaddingValues(start = 12.dp, end = 8.dp),
                                        modifier = Modifier
                                            .heightIn(min = 56.dp)
                                            .fillMaxWidth()
                                    ) {
                                        itemsIndexed(ivm.allIngredients.filter {
                                            it.tags.contains(
                                                tag
                                            )
                                        }
                                            .shuffled()) { index, ingredient ->
                                            SnackCard(
                                                ingredient = ingredient,
                                                onSnackClick = {
                                                    navigator.push(
                                                        IngredientDetailScreen(ingredient)
                                                    )
                                                },
                                                index = index,
                                                gradient = listOf(
                                                    MaterialTheme.colors.primary,
                                                    MaterialTheme.colors.secondary
                                                ),
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
}
