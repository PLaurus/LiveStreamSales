package com.example.livestreamsales.utils

import android.content.Context
import android.text.SpannableString
import android.text.SpannedString
import androidx.annotation.StringRes

interface IStringResAnnotationProcessor {
    fun process(context: Context, @StringRes resId: Int): SpannableString
    fun process(context: Context, text: SpannedString): SpannableString
}