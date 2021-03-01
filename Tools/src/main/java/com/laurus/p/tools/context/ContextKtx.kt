package com.laurus.p.tools.context

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getDrawableCompat(
    @DrawableRes drawableResId: Int,
    @ColorRes
    tintColorResId: Int? = null
): Drawable? {
    val drawable = ContextCompat.getDrawable(this, drawableResId)

    if(drawable != null && tintColorResId != null){
        val tintColor = ContextCompat.getColor(this, tintColorResId)
        DrawableCompat.setTint(drawable, tintColor)
    }

    return drawable
}