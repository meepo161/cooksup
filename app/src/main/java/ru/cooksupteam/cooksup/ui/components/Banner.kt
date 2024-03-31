import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.AdSize
import com.yandex.mobile.ads.common.ImpressionData

@Composable
fun Banner(id: Int) {
    AndroidView(factory = { context ->
        BannerAdView(context).apply {
            setAdUnitId(context.getString(id))
            setAdSize(BannerAdSize.stickySize(context, 300))
            val adRequest = AdRequest.Builder().build()
            setBannerAdEventListener(object : BannerAdEventListener{
                override fun onAdLoaded() {

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