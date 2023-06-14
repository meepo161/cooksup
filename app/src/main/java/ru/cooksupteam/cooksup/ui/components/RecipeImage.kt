package ru.cooksupteam.cooksup.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme


@Composable
fun RecipeImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    background: Color = CooksupTheme.colors.uiBackground,
    shape: RoundedCornerShape = CircleShape
) {
    CooksupSurface(
        color = CooksupTheme.colors.uiBackground,
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .width(400.dp)
                .height(400.dp),
            contentScale = ContentScale.Crop,
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error || state is AsyncImagePainter.State.Empty) {
//                Image(
//                    painter = painterResource(id = R.drawable.placeholder),
//                    contentDescription = stringResource(id = R.string.downloading),
//                    contentScale = ContentScale.FillHeight
//                )

//                Icon(
//                    Icons.Rounded.WifiOff,
//                    contentDescription = null,
//                    tint = Color.Gray
//                    )
                CircularProgressIndicator(color = CooksupTheme.colors.brand)
            } else {
                SubcomposeAsyncImageContent()
            }
        }
    }
}