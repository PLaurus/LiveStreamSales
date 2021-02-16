package tv.wfc.livestreamsales.application.tools.string

import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan

fun String.strikeThrough(
    start: Int = 0,
    end: Int = length - 1
): SpannableString {
    return SpannableString(this).apply {
        setSpan(StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}