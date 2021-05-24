package com.laurus.p.tools.string

import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import java.util.regex.Pattern

fun String.strikeThrough(
    start: Int = 0,
    end: Int = length - 1
): SpannableString {
    return SpannableString(this).apply {
        setSpan(StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

fun CharSequence.split(symbol: Char, partLength: Int, splitFromStart: Boolean = true): CharSequence{
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

    return splitStringBuilder
}

fun CharSequence.isEmail(): Boolean{
    return Pattern.matches("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", this)
}

fun CharSequence.isNotEmail(): Boolean{
    return !isEmail()
}