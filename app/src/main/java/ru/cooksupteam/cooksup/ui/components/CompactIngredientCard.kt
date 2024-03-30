package ru.cooksupteam.cooksup.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.cooksupteam.cooksup.Singleton.navigator
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.screens.IngredientDetailScreen

@SuppressLint("UnrememberedMutableState")
@Composable
fun CompactIngredientCard(
    ingredient: Ingredient,
    modifier: Modifier = Modifier,
    onClick: (Ingredient, Boolean) -> Unit
) {
    val isSelected = mutableStateOf(ingredient.selected)
    CooksupCard(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(4.dp)
            .clickable {
                navigator.push(IngredientDetailScreen(ingredient = ingredient))
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
                imageUrl = ingredient.pic,
                modifier = Modifier
                    .size(100.dp)
            )
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
            ) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ingredient.description,
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.caption,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Checkbox(
                checked = isSelected.value,
                onCheckedChange = {
                    onClick(ingredient, isSelected.value)
                    isSelected.value = !isSelected.value
                    ingredient.selected = isSelected.value
                },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}