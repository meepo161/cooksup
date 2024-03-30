package ru.cooksupteam.cooksup.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

@Composable
fun SearchAppBar(
    modifier: Modifier,
    titleText: String,
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    isSearchState: Boolean
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(isSearchState) {
        snapshotFlow { isSearchState }.collect { isSearchState ->
            if (isSearchState) {
                focusRequester.requestFocus()
            }
        }
    }
    TopAppBar(
        elevation = 0.dp,
        modifier = modifier,
        backgroundColor = Color.Transparent,
        title = {
            Text(
                text = titleText,
                color = CooksupTheme.colors.brand
            )
        },
        actions = {
            AnimatedVisibility(
                visible = isSearchState,
                enter = slideInHorizontally { 100 },
                exit = slideOutHorizontally { 1 }
            ) {
                TextField(modifier = Modifier
                    .fillMaxWidth().focusRequester(focusRequester),
                    value = text,
                    onValueChange = {
                        onTextChange(it)
                    },
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(ContentAlpha.medium),
                            text = "Фильтр",
                            color = CooksupTheme.colors.brand
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        color = CooksupTheme.colors.brand
                    ),
                    singleLine = true,
                    leadingIcon = {
                        IconButton(
                            modifier = Modifier.alpha(ContentAlpha.medium),
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = CooksupTheme.colors.brand
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (text.isNotEmpty()) {
                                    onTextChange("")
                                } else {
                                    onCloseClicked()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Icon",
                                tint = CooksupTheme.colors.brand
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearchClicked()
                        }
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = CooksupTheme.colors.brand
                    ))
            }

            AnimatedVisibility(
                visible = !isSearchState,
                enter = slideInHorizontally { 20 },
                exit = slideOutHorizontally { 1 }
            ) {
                IconButton(
                    onClick = {
                        onSearchClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = CooksupTheme.colors.brand
                    )
                }
            }
        }
    )
}