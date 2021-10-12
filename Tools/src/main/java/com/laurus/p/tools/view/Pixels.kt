package com.laurus.p.tools.view

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

fun Number.pixelsToDp(resources: Resources): Float {
    return this.toFloat() / calculateScreenLogicalDensity(resources)
}

fun Number.dpToPixels(resources: Resources): Float {
    return this.toFloat() * calculateScreenLogicalDensity(resources)
}

fun Number.pixelsToSp(resources: Resources): Float {
    return this.toFloat() / resources.displayMetrics.scaledDensity
}

fun Number.spToPixels(resources: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        toFloat(),
        resources.displayMetrics
    )
}

fun calculateScreenLogicalDensity(resources: Resources): Float {
    // or we can just return displayMetrics.density
    return resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT
}