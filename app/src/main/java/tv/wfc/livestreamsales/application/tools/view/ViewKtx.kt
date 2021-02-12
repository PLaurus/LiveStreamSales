package tv.wfc.livestreamsales.application.tools.view

import android.view.View

fun View.matchRootView(
    matchWidth: Boolean = true,
    matchHeight: Boolean = true
){
    layoutParams = layoutParams.apply {
        if(matchWidth){
            val rootViewWidth = rootView.width
            width = rootViewWidth
        }

        if(matchHeight){
            val rootViewHeight = rootView.height
            height = rootViewHeight
        }
    }
}