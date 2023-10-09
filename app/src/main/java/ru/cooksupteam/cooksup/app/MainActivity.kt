package ru.cooksupteam.cooksup.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.screens.MainScreen
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.utils.ConnectionState
import ru.cooksupteam.cooksup.utils.connectivityState
import ru.cooksupteam.cooksup.viewmodel.IngredientsViewModel
import ru.cooksupteam.cooksup.viewmodel.RecipeFullViewModel
import ru.cooksupteam.cooksup.viewmodel.UserViewModel
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability
import kotlin.time.Duration.Companion.seconds

lateinit var ivm: IngredientsViewModel
lateinit var rvm: RecipeFullViewModel
lateinit var uvm: UserViewModel

class MainActivity : ComponentActivity() {
    val updateType = AppUpdateType.FLEXIBLE
    lateinit var appUpdateManager: RuStoreAppUpdateManager

    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        appContext = applicationContext
        try {
            appUpdateManager = RuStoreAppUpdateManagerFactory.create(applicationContext)
            if (updateType == AppUpdateType.FLEXIBLE) {
                appUpdateManager.registerListener(installStateUpdateListener)
            }
            checkForAppUpdates()
        } catch (e: Exception) {
        }
        ivm = IngredientsViewModel()
        rvm = RecipeFullViewModel()
        uvm = UserViewModel()
        setContent {
            CooksupTheme {
                val connection by connectivityState()
                val isConnected = connection === ConnectionState.Available
                if (isConnected) {
                    Navigator(MainScreen())
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CooksupTheme.colors.uiBackground)
                            .padding(horizontal = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Center)
                                .offset(y = (-50).dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.nosignal),
                                contentDescription = "logo",
                                alignment = Center,
                                modifier = Modifier.size(300.dp)
                            )
//                            Icon(
//                                imageVector = Icons.Outlined.SignalWifiStatusbarConnectedNoInternet4,
//                                contentDescription = "logo",
//                                modifier = Modifier.size(300.dp)
//                            )
                            Text(
                                text = "Отсутствует интернет соединение",
                                color = CooksupTheme.colors.brand,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Text(
                                text = "Уупс, видимо что-то со связью",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private val installStateUpdateListener = InstallStateUpdateListener { state ->
        if (state.installStatus == InstallStatus.DOWNLOADED) {
            Toast.makeText(
                applicationContext,
                "Загрузка обновления завершена. Приложение перезапустится для обновления.",
                Toast.LENGTH_SHORT
            ).show()
            lifecycleScope.launch {
                delay(5.seconds)
                appUpdateManager.completeUpdate()
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Обновление завершено",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { throwable ->
                        Toast.makeText(
                            applicationContext,
                            "Ошибка $throwable ${state.installErrorCode}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
    }

    private fun checkForAppUpdates() {
        appUpdateManager
            .getAppUpdateInfo()
            .addOnSuccessListener { info ->
                val isUpdateAvailable =
                    info.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE
                val isUpdateAllowed = when (updateType) {
                    AppUpdateType.FLEXIBLE -> true
                    AppUpdateType.IMMEDIATE -> true
                    else -> false
                }
                if (isUpdateAvailable && isUpdateAllowed) {
                    appUpdateManager.registerListener(installStateUpdateListener)
                    appUpdateManager.startUpdateFlow(
                        info,
                        AppUpdateOptions.Builder().appUpdateType(AppUpdateType.FLEXIBLE).build()
                    )
                        .addOnSuccessListener { resultCode ->
                            if (resultCode != Activity.RESULT_OK) {
                                println("Something went wrong")
                            }
                        }
                }
            }
            .addOnFailureListener { error ->
//                Toast.makeText(
//                    applicationContext,
//                    "Что-то пошло не так: $error",
//                    Toast.LENGTH_LONG
//                ).show()
            }
    }
}
