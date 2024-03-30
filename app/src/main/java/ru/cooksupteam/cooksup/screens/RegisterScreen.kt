package ru.cooksupteam.cooksup.screens

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton.scope
import ru.cooksupteam.cooksup.app.uvm
import ru.cooksupteam.cooksup.isEmailValid
import ru.cooksupteam.cooksup.model.Person


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
            .background(MaterialTheme.colors.background)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
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
                        color = MaterialTheme.colors.primary
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
                textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    cursorColor = MaterialTheme.colors.primary,
                    focusedIndicatorColor = MaterialTheme.colors.primary,
                    unfocusedIndicatorColor = MaterialTheme.colors.primary
                )
            )

            OutlinedTextField(
                value = email.value,
                isError = emailError.value,
                onValueChange = { email.value = it },
                label = {
                    Text(
                        text = "Email",
                        color = MaterialTheme.colors.primary
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
                textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    cursorColor = MaterialTheme.colors.primary,
                    focusedIndicatorColor = MaterialTheme.colors.primary,
                    unfocusedIndicatorColor = MaterialTheme.colors.primary
                )
            )
            OutlinedTextField(
                value = password.value,
                isError = passwordError.value,
                onValueChange = { password.value = it },
                label = {
                    Text(
                        text = "Пароль",
                        color = MaterialTheme.colors.primary
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
                trailingIcon = {
                    IconButton(modifier = Modifier.indication(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = MaterialTheme.colors.primary)
                    ), onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "",
                            tint = if (passwordVisibility.value) MaterialTheme.colors.primary else Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    cursorColor = MaterialTheme.colors.primary,
                    focusedIndicatorColor = MaterialTheme.colors.primary,
                    unfocusedIndicatorColor = MaterialTheme.colors.primary,
                    disabledLabelColor = MaterialTheme.colors.primary,
                    focusedLabelColor = MaterialTheme.colors.primary,
                    placeholderColor = MaterialTheme.colors.primary,

                    )
            )

            OutlinedTextField(
                value = confirmPassword.value,
                isError = confirmPasswordError.value,
                onValueChange = { confirmPassword.value = it },
                label = {
                    Text(
                        text = "Подвердите пароль",
                        color = MaterialTheme.colors.primary
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
                        indication = rememberRipple(color = MaterialTheme.colors.primary)
                    ), onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "",
                            tint = if (passwordVisibility.value) MaterialTheme.colors.primary else Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    cursorColor = MaterialTheme.colors.primary,
                    focusedIndicatorColor = MaterialTheme.colors.primary,
                    unfocusedIndicatorColor = MaterialTheme.colors.primary
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
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.background
                )
            ) {
                Text(
                    text = "Зарегистрироваться",
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.background
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))

            Text(
                text = "Авторизация",
                modifier = Modifier.clickable(
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = MaterialTheme.colors.primary),
                    onClick = { uvm.loginState.value = true }),
                color = MaterialTheme.colors.primary
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
    uvm.loginState.value = true
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun FilterEnabledPreview() {
    MaterialTheme {
        RegisterPage()
    }
}
