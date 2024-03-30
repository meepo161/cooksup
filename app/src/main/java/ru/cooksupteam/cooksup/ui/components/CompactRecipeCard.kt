package ru.cooksupteam.cooksup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ru.cooksupteam.cooksup.model.Recipe

@Composable
fun CompactRecipeCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit, // Callback for favoriting action
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(recipe.pic),
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Larger image height
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.body2,
                    maxLines = 2, // Limit description to two lines
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onFavoriteClick
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MaterialTheme.colors.onSurface else MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}