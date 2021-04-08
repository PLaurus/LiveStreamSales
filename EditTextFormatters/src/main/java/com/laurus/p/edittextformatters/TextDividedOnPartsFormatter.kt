package com.laurus.p.edittextformatters

import android.text.Editable

class TextDividedOnPartsFormatter(
    private val partLength: Int,
    private val separator: Char = ' ',
    afterTextFormatted: ((noStyleText: CharSequence) -> Unit)? = null
): SeparatorsEditTextFormatter(afterTextFormatted) {
    @Synchronized
    override fun format(text: CharSequence, updateSeparatorsPositions: Boolean): CharSequence{
        val parts = text.windowed(partLength, partLength, true)

        val splitStringBuilder = StringBuilder()

        parts.forEachIndexed{ index, part ->
            splitStringBuilder.append(part)

            if(index != parts.size - 1){
                splitStringBuilder.append(separator)
                if(updateSeparatorsPositions)
                    addSeparatorPosition(splitStringBuilder.lastIndex)
            }
        }

        return splitStringBuilder
    }
}