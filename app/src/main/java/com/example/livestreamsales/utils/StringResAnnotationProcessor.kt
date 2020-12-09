package com.example.livestreamsales.utils

import android.content.Context
import androidx.annotation.StringRes

interface StringResAnnotationProcessor {
    fun process(context: Context, @StringRes resId: Int)
}