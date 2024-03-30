package ru.cooksupteam.cooksup.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest



@Composable
fun RecipeImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    background: Color = MaterialTheme.colors.background,
    shape: Shape = CircleShape
) {
    CooksupSurface(
        color = MaterialTheme.colors.background,
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
//                .crossfade(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            filterQuality = FilterQuality.None,
            contentDescription = "",
            modifier = Modifier
                .width(400.dp)
                .height(400.dp),
            contentScale = ContentScale.Crop,
        ) {
            if (painter.state is AsyncImagePainter.State.Loading || painter.state is AsyncImagePainter.State.Error) {
                AnimatedShimmer()
            } else {
                SubcomposeAsyncImageContent()
            }
        }
    }
}