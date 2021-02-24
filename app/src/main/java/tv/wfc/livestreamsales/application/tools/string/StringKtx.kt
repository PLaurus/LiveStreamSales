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

fun String.split(symbol: Char, partLength: Int, splitFromStart: Boolean = true): String{
    if(isEmpty()) return this
    if(partLength <= 0) return this

    val parts = windowed(partLength,partLength, true)

    val splitStringBuilder = StringBuilder()

    if(splitFromStart){
        parts.forEachIndexed{ index, part ->
            splitStringBuilder.append(part)

            if(index != parts.size - 1){
                splitStringBuilder.append(symbol)
            }
        }
    } else{
        parts.forEachIndexed{ index, part ->
            splitStringBuilder.insert(0, part)

            if(index != parts.size - 1){
                splitStringBuilder.insert(0, symbol)
            }
        }
    }

    return ""
}