package ru.cooksupteam.cooksup.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.cooksupteam.cooksup.model.Filter


@Composable
fun CooksupFilterChip(
    filter: Filter,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large
) {
    val (selected, setSelected) = filter.enabled
    val backgroundColor by animateColorAsState(
        if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.background
    )
    val border = Modifier.fadeInDiagonalGradientBorder(
        showBorder = !selected,
        colors = listOf(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.secondary
        ),
        shape = shape
    )
    val textColor by animateColorAsState(
        if (selected) Color.Black else MaterialTheme.colors.secondary
    )

    CooksupSurface(
        modifier = modifier.height(28.dp),
        color = backgroundColor,
        contentColor = textColor,
        shape = shape,
        elevation = 2.dp
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed) {
                Modifier.offsetGradientBackground(
                    listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.secondary
                    ),
                    200f,
                    0f
                )
            } else {
                Modifier.background(Color.Transparent)
            }
        Box(
            modifier = Modifier
                .toggleable(
                    value = selected,
                    onValueChange = setSelected,
                    interactionSource = interactionSource,
                    indication = null
                )
                .then(backgroundPressed)
                .then(border),
        ) {
            Text(
                text = filter.name,
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                )
            )
        }
    }
}


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun FilterDisabledPreview() {
    MaterialTheme {
        CooksupFilterChip(Filter(name = "Низкокалорийные", enabled = false), Modifier.padding(4.dp))
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun FilterEnabledPreview() {
    MaterialTheme {
        CooksupFilterChip(Filter(name = "Низкокалорийные", enabled = true))
    }
}
