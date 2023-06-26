package ru.cooksupteam.cooksup.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.cooksupteam.cooksup.Singleton.allIngredients
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.Singleton.isAuthorized
import ru.cooksupteam.cooksup.Singleton.loginState
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

class ProfileTab() : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.home_profile)
            val icon = rememberVectorPainter(Icons.Default.Person)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        var alphabet =
            allIngredients.map { it.name.first().uppercase() }.toSet().toList().toTypedArray()
        var scaffoldState = rememberScaffoldState()
        val scrollState = rememberLazyListState()

        CooksupTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .heightIn(max = 32.dp)
                            .background(CooksupTheme.colors.uiBackground)
                            .fillMaxWidth(),
                        title = {
                            Row(
                                modifier = Modifier
                                    .background(CooksupTheme.colors.uiBackground)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = stringResource(id = R.string.my_profile),
                                    color = CooksupTheme.colors.brand,
                                    fontSize = 22.sp
                                )
                            }
                        }, backgroundColor = CooksupTheme.colors.uiBackground
                    )
                },
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(CooksupTheme.colors.uiBackground)
                        .padding(bottom = 56.dp)
                ) {
                    if (isAuthorized.value) {
                        ProfileEcommerce()
                    } else {
                        if (loginState.value) {
                            LoginPage()
                        } else {
                            RegisterPage()
                        }
                    }
                }
            }
        }
    }

    private val optionsList: ArrayList<OptionsData> = ArrayList()

    @Composable
    fun ProfileEcommerce() {
        val listPrepared = remember {
            mutableStateOf(false)
        }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                optionsList.clear()
                prepareOptionsData()
                listPrepared.value = true
            }
        }

        if (listPrepared.value) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                item {
                    UserDetails(context = appContext)
                }

                items(optionsList) { item ->
                    OptionsItemStyle(item = item, context = appContext)
                }

            }
        }
    }

    @Composable
    private fun UserDetails(context: Context) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .size(72.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(id = R.drawable.moustache),
                contentDescription = "Your Image"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(weight = 3f, fill = false)
                        .padding(start = 16.dp)
                ) {

                    Text(
                        text = "Meepo Meepo",
                        style = TextStyle(
                            fontSize = 22.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = CooksupTheme.colors.brand
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "meepo161@email.com",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            letterSpacing = (0.8).sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Edit button
                IconButton(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false),
                    onClick = {
                        Toast.makeText(context, "Edit Button", Toast.LENGTH_SHORT).show()
                    }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Details",
                        tint = CooksupTheme.colors.brand
                    )
                }

            }
        }
    }

    // Row style for options
    @Composable
    private fun OptionsItemStyle(item: OptionsData, context: Context) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = CooksupTheme.colors.brand),
                    onClick = {
                        Toast
                            .makeText(context, item.title, Toast.LENGTH_SHORT)
                            .show()
                    })
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Icon
            Icon(
                modifier = Modifier
                    .size(32.dp),
                imageVector = item.icon,
                contentDescription = item.title,
                tint = CooksupTheme.colors.brand
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(weight = 3f, fill = false)
                        .padding(start = 16.dp)
                ) {

                    // Title
                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontSize = 18.sp,
                        ),
                        color = CooksupTheme.colors.brand
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Sub title
                    Text(
                        text = item.subTitle,
                        style = TextStyle(
                            fontSize = 14.sp,
                            letterSpacing = (0.8).sp,
                            color = Color.Gray
                        )
                    )

                }
                Icon(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false),
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = item.title,
                    tint = Color.Black.copy(alpha = 0.70f)
                )
            }

        }
    }

    private fun prepareOptionsData() {

        val appIcons = Icons.Outlined

        optionsList.add(
            OptionsData(
                icon = appIcons.Person,
                title = "Аккаунт",
                subTitle = "Настройте свой аккаунт"
            )
        )

        optionsList.add(
            OptionsData(
                icon = appIcons.FavoriteBorder,
                title = "Избранные",
                subTitle = "Избранные рецепты"
            )
        )

        optionsList.add(
            OptionsData(
                icon = appIcons.Settings,
                title = "Настройка",
                subTitle = "Настройки приложения"
            )
        )

        optionsList.add(
            OptionsData(
                icon = appIcons.Help,
                title = "Справочный центр",
                subTitle = "Часто задаваемые вопросы и поддержка клиентов"
            )
        )

    }

    data class OptionsData(val icon: ImageVector, val title: String, val subTitle: String)
}