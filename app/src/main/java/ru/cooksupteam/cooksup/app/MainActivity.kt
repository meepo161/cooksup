package ru.cooksupteam.cooksup.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.instream.MobileInstreamAds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.cooksupteam.cooksup.Singleton.appContext
import ru.cooksupteam.cooksup.screens.MainScreen
import ru.cooksupteam.cooksup.ui.theme.CooksupTheme
import ru.cooksupteam.cooksup.viewmodel.IngredientsViewModel
import ru.cooksupteam.cooksup.viewmodel.MainViewModel
import ru.cooksupteam.cooksup.viewmodel.RecipeViewModel
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability
import java.io.File
import java.net.URLDecoder
import kotlin.time.Duration.Companion.seconds


lateinit var ivm: IngredientsViewModel
lateinit var rvm: RecipeViewModel
lateinit var mvm: MainViewModel
var path = Environment.getExternalStoragePublicDirectory(
    Environment.DIRECTORY_DOCUMENTS
)
lateinit var yandexBannerAd: BannerAdView

class MainActivity : ComponentActivity() {
    val updateType = AppUpdateType.FLEXIBLE
    lateinit var appUpdateManager: RuStoreAppUpdateManager


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("SourceLockedOrientationActivity", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        appContext = applicationContext
        try {
            appUpdateManager = RuStoreAppUpdateManagerFactory.create(applicationContext)
            appUpdateManager.registerListener(installStateUpdateListener)
            checkForAppUpdates()
        } catch (_: Exception) {
        }

        MobileAds.initialize(this) { }
        MobileInstreamAds.setAdGroupPreloading(true)
        MobileAds.enableLogging(true)
        yandexBannerAd = BannerAdView(this)

        ivm = IngredientsViewModel()
        rvm = RecipeViewModel()
        mvm = MainViewModel()
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
        val convertedString = URLDecoder.decode(nameRecipeIntent, "UTF-8")
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
                Toast.makeText(
                    applicationContext,
                    "Что-то пошло не так: $error",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
