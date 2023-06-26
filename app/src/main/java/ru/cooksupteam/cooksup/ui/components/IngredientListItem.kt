package ru.cooksupteam.cooksup.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import ru.cooksupteam.cooksup.Singleton.lastIndexIngredient
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.screens.IngredientDetailScreen
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

@SuppressLint("UnrememberedMutableState")
@Composable
fun IngredientListItem(
    ingredient: Ingredient,
    index: Int,
    navigator: Navigator,
    onClick: (Ingredient, Boolean) -> Unit
) {
    val isSelected = mutableStateOf(ingredient.selected)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = CooksupTheme.colors.uiBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = CooksupTheme.colors.brand),
                onClick = {
                    lastIndexIngredient = index + 1
                    navigator.push(IngredientDetailScreen(ingredient))
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(
            32.dp,
            Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IngredientImage(
            modifier = Modifier
                .diagonalGradientBorder(
                    colors = CooksupTheme.colors.gradient3_2,
                    shape = MaterialTheme.shapes.medium
                )
                .size(48.dp),
            imageUrl = ingredient.pic
        )
        Text(
            text = ingredient.name,
            color = CooksupTheme.colors.textPrimary,
            modifier = Modifier.weight(0.9f)
        )
        Box(
            modifier = Modifier
                .weight(0.1f)
                .clickable {
                    onClick(ingredient, isSelected.value)
                    isSelected.value = !isSelected.value
                    ingredient.selected = isSelected.value
                },
        ) {
            Icon(
                if (!isSelected.value) Icons.Rounded.AddCircle else Icons.Rounded.RemoveCircle,
                contentDescription = null,

                tint = if (!isSelected.value) CooksupTheme.colors.brand else CooksupTheme.colors.error,
            )
        }
    }
}
