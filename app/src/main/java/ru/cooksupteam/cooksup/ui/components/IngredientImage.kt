package ru.cooksupteam.cooksup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme


@Composable
fun IngredientImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    background: Color = CooksupTheme.colors.uiBackground
) {
    CooksupSurface(
        color = CooksupTheme.colors.uiBackground,
        elevation = elevation,
        shape = CircleShape,
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
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = stringResource(id = R.string.downloading),
                    contentScale = ContentScale.FillHeight
                )
//                CircularProgressIndicator()
            } else {
                SubcomposeAsyncImageContent()
            }
        }
    }
}