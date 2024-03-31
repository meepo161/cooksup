//package ru.cooksupteam.cooksup.ui.components
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.viewinterop.AndroidView
//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.AdSize
//import com.google.android.gms.ads.AdView
//import com.google.android.gms.ads.LoadAdError
//
//@Composable
//fun AdMobBanner(id: Int) {
//    AndroidView(factory = { context ->
//        AdView(context).apply {
//            setAdSize(AdSize.BANNER)
//            adUnitId = context.getString(id)
//            val adRequest = AdRequest.Builder().build()
//            adListener = object : AdListener(){
//                override fun onAdClicked() {
//                    super.onAdClicked()
//                }
//
//                override fun onAdClosed() {
//                    super.onAdClosed()
//                }
//
//                override fun onAdFailedToLoad(p0: LoadAdError) {
//                    super.onAdFailedToLoad(p0)
//                }
//
//                override fun onAdImpression() {
//                    super.onAdImpression()
//                }
//
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
//                }
//
//                override fun onAdOpened() {
//                    super.onAdOpened()
//                }
//
//                override fun onAdSwipeGestureClicked() {
//                    super.onAdSwipeGestureClicked()
//                }
//            }
//            loadAd(adRequest)
//        }
//    })
//}