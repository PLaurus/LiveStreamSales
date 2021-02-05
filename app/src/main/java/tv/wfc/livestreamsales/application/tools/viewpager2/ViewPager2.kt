package tv.wfc.livestreamsales.application.tools.viewpager2

import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.onPageSelected(callback: (position: Int) -> Unit): ViewPager2.OnPageChangeCallback{
    val callbackObject = object: ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            callback(position)
        }
    }

    registerOnPageChangeCallback(callbackObject)

    return callbackObject
}