package com.laurus.p.edittextformatters

import android.text.TextWatcher

interface IEditTextFormatter: TextWatcher {
    fun format(text: CharSequence): CharSequence
}