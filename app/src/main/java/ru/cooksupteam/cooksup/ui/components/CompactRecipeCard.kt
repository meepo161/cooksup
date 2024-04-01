package ru.cooksupteam.cooksup.ui.components

import android.annotation.SuppressLint
import android.text.style.ClickableSpan
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.app.rvm
import ru.cooksupteam.cooksup.model.Recipe
import ru.cooksupteam.cooksup.screens.RecipeDetailScreen
import ru.cooksupteam.cooksup.screens.RecipesFavoriteScreen
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

@SuppressLint("UnrememberedMutableState")
@Composable
fun CompactRecipeCard(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    index: Int,
    onFavoriteClick: (Recipe, Boolean) -> Unit
) {
    Log.d("favorite", rvm.favoriteRecipe.contains(recipe).toString())
    val isFavorite = mutableStateOf(rvm.favoriteRecipe.contains(recipe))
    CooksupCard(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(4.dp)
            .clickable {
                rvm.lastIndexRecipe = index
                navigator.push(RecipeDetailScreen(Gson().toJson(recipe)))
            },
        elevation = 2.dp,
        shape = RoundedCornerShape(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            IngredientImage(
                imageUrl = recipe.pic,
                modifier = Modifier
                    .size(100.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(
                    text = recipe.name,
                    color = CooksupTheme.colors.brandSecondary,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = recipe.description,
                    color = CooksupTheme.colors.brand,
                    style = MaterialTheme.typography.caption,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {
                    onFavoriteClick(recipe, isFavorite.value)
                    isFavorite.value = !isFavorite.value
                }) {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(32.dp),
                    imageVector = if(isFavorite.value) Icons.Filled.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = "",
                    tint = CooksupTheme.colors.error
                )
            }
        }
    }
}

