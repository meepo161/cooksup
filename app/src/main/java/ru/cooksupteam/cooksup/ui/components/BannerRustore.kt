import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.my.target.common.NavigationType
import com.my.target.nativeads.NativeAd
import com.my.target.nativeads.banners.NativePromoBanner
import com.my.target.nativeads.factories.NativeViewsFactory
import com.my.target.nativeads.views.IconAdView
import com.my.target.nativeads.views.NativeAdContainer
import com.my.target.nativeads.views.NativeAdView
import ru.cooksupteam.cooksup.app.R
import ru.cooksupteam.cooksup.app.ruStoreNativeAd


@SuppressLint("ResourceType")
@Composable
fun BannerRustore() {
    AndroidView(factory = { context ->
        NativeAdView(context).apply {
            ruStoreNativeAd!!.listener = object : NativeAd.NativeAdListener {
                override fun onLoad(p0: NativePromoBanner, p1: NativeAd) {
                    onLoad(ruStoreNativeAd!!.banner!!, ruStoreNativeAd!!, context)
                }
                override fun onNoAd(p0: String, p1: NativeAd) {}
                override fun onClick(p0: NativeAd) {}
                override fun onShow(p0: NativeAd) {}
                override fun onVideoPlay(p0: NativeAd) {}
                override fun onVideoPause(p0: NativeAd) {}
                override fun onVideoComplete(p0: NativeAd) {}
            }
            ruStoreNativeAd!!.load()
        }
    })
}

fun onLoad(banner: NativePromoBanner, ad: NativeAd, context: Context) {
    val title = banner.getTitle()
    val description = banner.getDescription()
    val ageRestrictions = banner.getAgeRestrictions()
    val disclaimer = banner.getDisclaimer()
    val advertisingLabel = banner.getAdvertisingLabel()

    val icon = banner.getIcon()
    val ctaText = banner.getCtaText()
    if (banner.getNavigationType() == NavigationType.STORE) {
        val rating = banner.getRating()
        val votes = banner.getVotes()
        val category = banner.category
        val subcategory: String = banner.subCategory ?: ""
    } else if (banner.getNavigationType() == NavigationType.WEB) {
        val domain = banner.getDomain()
    }

    val adViewLayout = LinearLayout(context)
    adViewLayout.setId(R.id.nativeads_ad_view)
    val titleView = TextView(context)
    titleView.setId(R.id.nativeads_title)
    titleView.text = title
    adViewLayout.addView(titleView)
    val descriptionView = TextView(context)
    descriptionView.setId(R.id.nativeads_description)
    titleView.text = description
    adViewLayout.addView(descriptionView)
    val btn = Button(context)
    btn.setId(R.id.nativeads_call_to_action)
    btn.text = ctaText
    adViewLayout.addView(btn)

    val mediaView = NativeViewsFactory.getMediaAdView(context)
    mediaView.setId(R.id.nativeads_media_view)
    val iconView = IconAdView(context)
    mediaView.setId(R.id.nativeads_icon)
    adViewLayout.addView(mediaView)
    adViewLayout.addView(iconView)
    val nativeAdContainer = NativeAdContainer(context)
    nativeAdContainer.addView(adViewLayout)
    ad.registerView(nativeAdContainer)

    adViewLayout.addView(
        nativeAdContainer,
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

