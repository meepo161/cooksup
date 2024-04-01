//package ru.cooksupteam.cooksup.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.Button
//import androidx.compose.material.ButtonDefaults
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.Text
//import androidx.compose.material.TextFieldDefaults
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material.ripple.rememberRipple
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.focusRequester
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.launch
//import ru.cooksupteam.cooksup.RESTAPI
//import ru.cooksupteam.cooksup.Singleton
//import ru.cooksupteam.cooksup.Singleton.scope
//import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
//import java.io.File
//
//
//@Composable
//fun LoginPage() {
//    val loginValue = remember { mutableStateOf("") }
//    val passwordValue = remember { mutableStateOf("") }
//
//    val passwordVisibility = remember { mutableStateOf(false) }
//    val focusRequester = remember { FocusRequester() }
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(CooksupTheme.colors.uiBackground)
//            .padding(12.dp)
//    ) {
//
//        Text(
//            text = "Авторизация",
//            style = MaterialTheme.typography.h4,
//            color = CooksupTheme.colors.brand,
//            fontSize = 30.sp
//        )
//        Spacer(modifier = Modifier.padding(20.dp))
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            OutlinedTextField(
//                value = loginValue.value,
//                onValueChange = { loginValue.value = it },
//                singleLine = true,
//                label = {
//                    Text(
//                        text = "Email",
//                        color = CooksupTheme.colors.brand
//                    )
//                },
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Email,
//                    imeAction = ImeAction.Next
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = {
//                        focusRequester.requestFocus()
//                    }
//                ),
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .focusRequester(focusRequester = focusRequester),
//                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
//                colors = TextFieldDefaults.textFieldColors(
//                    backgroundColor = CooksupTheme.colors.uiBackground,
//                    cursorColor = CooksupTheme.colors.brand,
//                    focusedIndicatorColor = CooksupTheme.colors.brand,
//                    unfocusedIndicatorColor = CooksupTheme.colors.brand
//                )
//            )
//            OutlinedTextField(
//                value = passwordValue.value,
//                onValueChange = { passwordValue.value = it },
//                trailingIcon = {
//                    IconButton(onClick = {
//                        passwordVisibility.value = !passwordVisibility.value
//                    }) {
//                        Icon(
//                            imageVector = if (passwordVisibility.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                            contentDescription = "",
//                            tint = if (passwordVisibility.value) CooksupTheme.colors.brand else Color.Gray
//                        )
//                    }
//                },
//                singleLine = true,
//                label = {
//                    Text(
//                        text = "Пароль",
//                        color = CooksupTheme.colors.brand
//                    )
//                },
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Text,
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(
//                    onDone = {
////                        authorize(loginValue, passwordValue)
//                    }
//                ),
//                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
//                else PasswordVisualTransformation(),
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .focusRequester(focusRequester = focusRequester),
//                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
//                colors = TextFieldDefaults.textFieldColors(
//                    backgroundColor = CooksupTheme.colors.uiBackground,
//                    cursorColor = CooksupTheme.colors.brand,
//                    focusedIndicatorColor = CooksupTheme.colors.brand,
//                    unfocusedIndicatorColor = CooksupTheme.colors.brand
//                )
//
//            )
//
//            Spacer(modifier = Modifier.padding(12.dp))
//            Button(
//                onClick = {
////                    authorize(loginValue, passwordValue)
//                },
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .height(50.dp),
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = CooksupTheme.colors.brand,
//                    contentColor = CooksupTheme.colors.uiBackground
//                )
//            ) {
//                Text(
//                    text = "Вход",
//                    fontSize = 20.sp,
//                    color = CooksupTheme.colors.uiBackground
//                )
//            }
//
//            Spacer(modifier = Modifier.padding(20.dp))
//            Text(
//                text = "Регистрация",
//                modifier = Modifier.clickable(
//                    enabled = true,
//                    interactionSource = remember { MutableInteractionSource() },
//                    indication = rememberRipple(color = CooksupTheme.colors.brand),
//                    onClick = { uvm.loginState.value = false }),
//                color = CooksupTheme.colors.brand
//            )
//        }
//    }
//}
