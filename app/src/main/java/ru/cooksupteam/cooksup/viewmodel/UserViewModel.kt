package ru.cooksupteam.cooksup.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI
import ru.cooksupteam.cooksup.Singleton
import ru.cooksupteam.cooksup.model.Person
import java.io.File

class UserViewModel {
    var user = Person()
    var loginState = mutableStateOf(true)
    var isAuthorized = mutableStateOf(false)

    init {
        val file = File(Singleton.appContext.filesDir, "id.txt")
        var id = ""
        if (!file.exists()) {
            file.createNewFile()
        }
        id = file.readText()
        Singleton.scope.launch {
            try {
                user = RESTAPI.fetchAuthPerson(id)
                isAuthorized.value = true
            } catch (e: Exception) {
                isAuthorized.value = false
            }
        }
    }
}