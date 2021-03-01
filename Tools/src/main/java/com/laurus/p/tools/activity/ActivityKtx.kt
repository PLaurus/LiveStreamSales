package com.laurus.p.tools.activity

import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.view.WindowInsets

@Suppress("DEPRECATION")
fun Activity.getWindowSize(): Pair<Int, Int>{
    val width: Int
    val height: Int

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val windowInsets = windowMetrics.windowInsets

        val insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout())
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom

        val bounds = windowMetrics.bounds
        width = bounds.width() - insetsWidth
        height = bounds.height() - insetsHeight
    } else {
        val size = Point()
        val display = windowManager.defaultDisplay // deprecated in API 30
        display?.getSize(size) // deprecated in API 30
        width = size.x
        height = size.y
    }

    return width to height
}