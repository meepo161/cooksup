package ru.cooksupteam.cooksup.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import com.google.gson.Gson
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.instream.MobileInstreamAds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.RESTAPI.fetchRecipeFilteredFromText
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.screens.MainScreen
import ru.cooksupteam.cooksup.screens.RecipeDetailScreen
import ru.cooksupteam.cooksup.toUTF8
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.viewmodel.IngredientsViewModel
import ru.cooksupteam.cooksup.viewmodel.RecipeViewModel
import ru.cooksupteam.cooksup.viewmodel.UserViewModel
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability
import java.io.File
import java.lang.Thread.sleep
import java.net.URLDecoder
import java.nio.charset.Charset
import kotlin.time.Duration.Companion.seconds


lateinit var ivm: IngredientsViewModel
lateinit var rvm: RecipeViewModel
lateinit var uvm: UserViewModel
var path = Environment.getExternalStoragePublicDirectory(
    Environment.DIRECTORY_DOCUMENTS
)
lateinit var yandexBannerAd: BannerAdView


class MainActivity : ComponentActivity() {
    val updateType = AppUpdateType.FLEXIBLE
    lateinit var appUpdateManager: RuStoreAppUpdateManager


    @SuppressLint("SourceLockedOrientationActivity", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) { }
        MobileInstreamAds.setAdGroupPreloading(true)
        MobileAds.enableLogging(true)
        yandexBannerAd = BannerAdView(this)
        yandexBannerAd.id = R.string.banner_recipes_tab
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        appContext = applicationContext
        try {
            appUpdateManager = RuStoreAppUpdateManagerFactory.create(applicationContext)
            if (updateType == AppUpdateType.FLEXIBLE) {
                appUpdateManager.registerListener(installStateUpdateListener)
            }
            checkForAppUpdates()
        } catch (_: Exception) {
        }
        ivm = IngredientsViewModel()
        rvm = RecipeViewModel()
        uvm = UserViewModel()
        val firstTime = File(path, "fisrtTime.txt")
        try {
            firstTime.readText() != "1"
        } catch (e: Exception) {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
            firstTime.createNewFile()
            firstTime.writeText("1")
        }
        val data: CharSequence? = intent?.data?.toString()
        val nameRecipeIntent = data.toString().replace("cooksup://recipe/", "")
        var convertedString = URLDecoder.decode(nameRecipeIntent, "UTF-8")
        Log.d("RecipeDataIntent", convertedString)
//        if (data.isNullOrBlank()) {
            setContent {
                CooksupTheme {
                    Navigator(MainScreen())
                }
            }
//        } else {
//            while (rvm.allRecipes.size < 90000) {
//                sleep(10)
//            }
//            setContent {
//                CooksupTheme {
//                    Navigator(RecipeDetailScreen(Gson().toJson(fetchRecipeFilteredFromText(convertedString).first())))
//                }
//            }
//        }

    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("Intent12345", intent.dataString ?: "12345")
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
