package ru.cooksupteam.cooksup.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.loginState
import ru.cooksupteam.cooksup.Singleton.scope
import ru.cooksupteam.cooksup.isEmailValid
import ru.cooksupteam.cooksup.model.Person
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme

@Composable
fun RegisterPage() {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val nameError = remember { mutableStateOf(false) }
    val emailError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }
    val confirmPasswordError = remember { mutableStateOf(false) }
    val person: Person
    val passwordVisibility = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CooksupTheme.colors.uiBackground)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.h4,
            color = CooksupTheme.colors.brand,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.padding(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = name.value,
                isError = nameError.value,
                onValueChange = { name.value = it },
                label = {
                    Text(
                        text = "Имя",
                        color = CooksupTheme.colors.brand
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = CooksupTheme.colors.uiBackground,
                    cursorColor = CooksupTheme.colors.brand,
                    focusedIndicatorColor = CooksupTheme.colors.brand,
                    unfocusedIndicatorColor = CooksupTheme.colors.brand
                )
            )

            OutlinedTextField(
                value = email.value,
                isError = emailError.value,
                onValueChange = { email.value = it },
                label = {
                    Text(
                        text = "Email",
                        color = CooksupTheme.colors.brand
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = CooksupTheme.colors.uiBackground,
                    cursorColor = CooksupTheme.colors.brand,
                    focusedIndicatorColor = CooksupTheme.colors.brand,
                    unfocusedIndicatorColor = CooksupTheme.colors.brand
                )
            )
            OutlinedTextField(
                value = password.value,
                isError = passwordError.value,
                onValueChange = { password.value = it },
                label = {
                    Text(
                        text = "Пароль",
                        color = CooksupTheme.colors.brand
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                trailingIcon = {
                    IconButton(modifier = Modifier.indication(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = CooksupTheme.colors.brand)
                    ), onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "",
                            tint = if (passwordVisibility.value) CooksupTheme.colors.brand else Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = CooksupTheme.colors.uiBackground,
                    cursorColor = CooksupTheme.colors.brand,
                    focusedIndicatorColor = CooksupTheme.colors.brand,
                    unfocusedIndicatorColor = CooksupTheme.colors.brand,
                    disabledLabelColor = CooksupTheme.colors.brand,
                    focusedLabelColor = CooksupTheme.colors.brand,
                    placeholderColor = CooksupTheme.colors.brand,

                    )
            )

            OutlinedTextField(
                value = confirmPassword.value,
                isError = confirmPasswordError.value,
                onValueChange = { confirmPassword.value = it },
                label = {
                    Text(
                        text = "Подвердите пароль",
                        color = CooksupTheme.colors.brand
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        register(
                            name = name,
                            nameError = nameError,
                            email = email,
                            emailError = emailError,
                            password = password,
                            passwordError = passwordError,
                            confirmPassword = confirmPassword,
                            confirmPasswordError = confirmPasswordError
                        )
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                trailingIcon = {
                    IconButton(modifier = Modifier.indication(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = CooksupTheme.colors.brand)
                    ), onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "",
                            tint = if (passwordVisibility.value) CooksupTheme.colors.brand else Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                textStyle = MaterialTheme.typography.h6.copy(color = CooksupTheme.colors.brand),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = CooksupTheme.colors.uiBackground,
                    cursorColor = CooksupTheme.colors.brand,
                    focusedIndicatorColor = CooksupTheme.colors.brand,
                    unfocusedIndicatorColor = CooksupTheme.colors.brand
                )
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Button(
                onClick = {
                    register(
                        name = name,
                        nameError = nameError,
                        email = email,
                        emailError = emailError,
                        password = password,
                        passwordError = passwordError,
                        confirmPassword = confirmPassword,
                        confirmPasswordError = confirmPasswordError
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = CooksupTheme.colors.brand,
                    contentColor = CooksupTheme.colors.uiBackground
                )
            ) {
                Text(
                    text = "Зарегистрироваться",
                    fontSize = 20.sp,
                    color = CooksupTheme.colors.uiBackground
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))

            Text(
                text = "Авторизация",
                modifier = Modifier.clickable(
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = CooksupTheme.colors.brand),
                    onClick = { loginState.value = true }),
                color = CooksupTheme.colors.brand
            )

        }

    }
}

private fun register(
    name: MutableState<String>,
    nameError: MutableState<Boolean>,
    email: MutableState<String>,
    emailError: MutableState<Boolean>,
    password: MutableState<String>,
    passwordError: MutableState<Boolean>,
    confirmPassword: MutableState<String>,
    confirmPasswordError: MutableState<Boolean>
) {
    if (validate(
            name,
            nameError,
            email,
            emailError,
            password,
            passwordError,
            confirmPassword,
            confirmPasswordError
        )
    ) {
        postPerson(
            person = Person(
                name = name.value,
                email = email.value,
                password = password.value
            )
        )
    }
}

private fun validate(
    name: MutableState<String>,
    nameError: MutableState<Boolean>,
    email: MutableState<String>,
    emailError: MutableState<Boolean>,
    password: MutableState<String>,
    passwordError: MutableState<Boolean>,
    confirmPassword: MutableState<String>,
    confirmPasswordError: MutableState<Boolean>
): Boolean {
    nameError.value = false
    emailError.value = false
    passwordError.value = false
    confirmPasswordError.value = false

    if (name.value == "") {
        nameError.value = true
    }
    if (!email.value.isEmailValid()) {
        emailError.value = true
    }
    if (password.value == "") {
        passwordError.value = true
    }
    if (confirmPassword.value != password.value || confirmPassword.value == "") {
        confirmPasswordError.value = true
    }

    return !nameError.value && !emailError.value && !passwordError.value && !confirmPasswordError.value
}

private fun postPerson(
    person: Person
) {
    scope.launch {
        RESTAPI.postPerson(
            Person(
                id = "",
                name = person.name,
                email = person.email,
                password = person.password
            )
        )
    }
    loginState.value = true
}
