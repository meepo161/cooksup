import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.app.mvm

@Composable
fun Banner(id: Int = R.string.banner_main_tab) {
    AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
        BannerAdView(context).apply {
            setAdUnitId(context.getString(id))
            setAdSize(BannerAdSize.stickySize(context, Int.MAX_VALUE))
            val adRequest = AdRequest.Builder().build()
            setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                    mvm.adsLoaded.value = true
                }

                override fun onAdFailedToLoad(p0: AdRequestError) {

                }

                override fun onAdClicked() {

                }

                override fun onLeftApplication() {

                }

                override fun onReturnedToApplication() {

                }

                override fun onImpression(p0: ImpressionData?) {

                }

            })
            loadAd(adRequest)
        }
    })
}