package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.Singleton.allIngredients
import ru.cooksupteam.cooksup.Singleton.selectedIngredients
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.ui.components.IngredientListItem
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import java.lang.Math.abs

class SearchTab(var navigator: Navigator) : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.search_tab)
            val icon = rememberVectorPainter(Icons.Default.Search)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
    @Composable
    override fun Content() {
        val navigatorTab = LocalNavigator.currentOrThrow
        val selectedHeader = remember { mutableStateOf("") }
        val listState = rememberLazyListState()
        var isNeedSelectedHeader by remember { mutableStateOf(false) }
        val offsets = remember { mutableStateMapOf<Int, Float>() }
        var selectedHeaderIndex by remember { mutableStateOf(0) }
        val scope = rememberCoroutineScope()
        val searchTextState = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

        val items = mutableStateListOf(*allIngredients.sortedBy { it.name.lowercase() }
            .filter { it.name.lowercase().contains(searchTextState.value.lowercase().trim()) }
            .toTypedArray())
        val headers = mutableStateListOf(
            *items.map { it.name.first().uppercase() }.toSet().toList().toTypedArray()
        )

        CooksupTheme {
            Scaffold(
                modifier = Modifier.padding(bottom = 56.dp),
                backgroundColor = CooksupTheme.colors.uiBackground,
                topBar = {
                    TopAppBar(
                        elevation = 0.dp,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = CooksupTheme.colors.uiBackground,
                        title = {
                            TextField(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Icon",
                                        tint = CooksupTheme.colors.brand,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Поиск ингредиентов",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp),
                                        textAlign = TextAlign.Start,
                                        fontSize = 20.sp,
                                        color = CooksupTheme.colors.textPrimary
                                    )
                                },
                                trailingIcon = {
                                    if (searchTextState.value !== "") {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close Icon",
                                            tint = CooksupTheme.colors.brand,
                                            modifier = Modifier
                                                .padding(horizontal = 12.dp)
                                                .clickable {
                                                    searchTextState.value = ""
                                                }
                                        )
                                    }
                                },
                                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
                                value = searchTextState.value,
                                onValueChange = {
                                    searchTextState.value = it
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = { navigatorTab.push(RecipesTab(navigator)) }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CooksupTheme.colors.uiBackground),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = CooksupTheme.colors.uiBackground,
                                    cursorColor = CooksupTheme.colors.brand,
                                    focusedIndicatorColor = CooksupTheme.colors.brand,
                                    unfocusedIndicatorColor = CooksupTheme.colors.brand
                                )
                            )

                        })
                }) {
                Row {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .scrollable(
                                rememberScrollableState {
                                    isNeedSelectedHeader = false
                                    it
                                },
                                orientation = Orientation.Vertical,
                            )
                            .weight(0.9f)
                            .background(CooksupTheme.colors.uiBackground),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        item {
                            Text(
                                text = "Всего ингредиентов: ${
                                    items.size
                                }",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                textAlign = TextAlign.Start,
                                color = CooksupTheme.colors.textPrimary
                            )
                        }
                        itemsIndexed(items) { index, ingredient ->
                            if (listState.isScrollInProgress) {
                                keyboardController?.hide()
                            }
                            IngredientListItem(ingredient, navigator) { _, isSelected ->
                                if (!isSelected) {
                                    selectedIngredients.add(ingredient)
                                } else {
                                    selectedIngredients.remove(ingredient)
                                }
                                isNeedSelectedHeader = false
                            }
                        }
                    }

                    fun updateSelectedIndexIfNeeded(offset: Float) {
                        val index = offsets
                            .mapValues { abs(it.value - offset) }
                            .entries
                            .minByOrNull { it.value }
                            ?.key ?: return
                        if (selectedHeaderIndex == index) return
                        selectedHeaderIndex = index
                        val selectedItemIndex = items.indexOfFirst {
                            it.name.first().uppercase() == headers[selectedHeaderIndex]
                        } + 1
                        scope.launch {
                            listState.scrollToItem(selectedItemIndex)
                            isNeedSelectedHeader = true
                            selectedHeader.value =
                                items[selectedItemIndex].name.first().uppercase()
                        }
                    }

                    if (searchTextState.value == "") {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.1f)
                                .padding(end = 12.dp)
                                .background(CooksupTheme.colors.uiBackground)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        updateSelectedIndexIfNeeded(it.y)
                                    }
                                }
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures { change, _ ->
                                        updateSelectedIndexIfNeeded(change.position.y)
                                    }
                                }
                        ) {
                            Box(
                                modifier = Modifier,
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                if (!listState.isScrollInProgress && isNeedSelectedHeader) {
                                    Text(
                                        text = selectedHeader.value,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                Color.Gray,
                                                shape = RoundedCornerShape(16.dp)
                                            ),
                                        textAlign = TextAlign.Center,
                                        fontSize = 28.sp,
                                        color = CooksupTheme.colors.uiBackground,
                                    )
                                } else {
                                    Text(
                                        text = "",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            headers.forEachIndexed { i, header ->
                                Text(
                                    header,
                                    modifier = Modifier.onGloballyPositioned {
                                        offsets[i] = it.boundsInParent().center.y
                                    },
                                    color = CooksupTheme.colors.brand
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}