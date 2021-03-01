package com.laurus.p.tools.edittext

import android.text.InputFilter
import android.widget.EditText

fun EditText.addFilter(filter: InputFilter){
    val newFilters = filters
        .toMutableList()
        .apply {
            add(filter)
        }
        .toTypedArray()

    filters = newFilters
}

fun EditText.removeFilter(filter: InputFilter){
    val newFilters = filters
        .toMutableList()
        .apply {
            remove(filter)
        }
        .toTypedArray()

    filters = newFilters
}