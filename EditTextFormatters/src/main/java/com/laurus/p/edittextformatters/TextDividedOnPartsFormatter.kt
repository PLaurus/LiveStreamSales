package com.laurus.p.edittextformatters

import android.text.Editable

class TextDividedOnPartsFormatter(
    private val partLength: Int,
    private val separator: Char = ' ',
    private val afterTextChanged: ((noStyleText: CharSequence) -> Unit)? = null
): IEditTextFormatter {
    private var selfChanged = false
    private val separatorsPositions: MutableList<Int> = mutableListOf()

    override fun format(text: CharSequence): CharSequence {
        return format(text, updateSeparatorsPositions = false)
    }

    @Synchronized
    override fun beforeTextChanged(
        currentText: CharSequence?,
        changeStart: Int,
        countOfSymbolsToBeReplaced: Int,
        replacementLength: Int
    ){
        if(selfChanged) return

        val changeEnd = changeStart + countOfSymbolsToBeReplaced

        val uselessSeparatorsPositions = mutableListOf<Int>()

        separatorsPositions.forEach { separatorPosition ->
            if(separatorPosition in changeStart..changeEnd){
                uselessSeparatorsPositions.add(separatorPosition)
            }
        }

        uselessSeparatorsPositions.forEach { uselessPosition ->
            separatorsPositions.removeAll { it == uselessPosition }
        }
    }

    @Synchronized
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    @Synchronized
    override fun afterTextChanged(s: Editable?) {
        if(selfChanged){
            selfChanged = false
            return
        }

        s?.run{
            val previousText = s.toString()
            val previousNotFormatted = previousText.removeSeparators()
            val formattedText = format(previousNotFormatted, updateSeparatorsPositions = true)
            val savedFilters = filters
            filters = arrayOf()
            selfChanged = true
            s.replace(0, s.length, formattedText)
            filters = savedFilters
            afterTextChanged?.invoke(formattedText.removeSeparators())
        }
    }

    @Synchronized
    private fun format(text: CharSequence, updateSeparatorsPositions: Boolean): CharSequence{
        val parts = text.windowed(partLength, partLength, true)

        val splitStringBuilder = StringBuilder()

        if(updateSeparatorsPositions){
            separatorsPositions.clear()
        }

        parts.forEachIndexed{ index, part ->
            splitStringBuilder.append(part)

            if(index != parts.size - 1){
                splitStringBuilder.append(separator)
                if(updateSeparatorsPositions)
                    separatorsPositions.add(splitStringBuilder.lastIndex)
            }
        }

        return splitStringBuilder
    }

    private fun CharSequence.removeSeparators(): CharSequence{
        var result = this

        separatorsPositions.forEachIndexed { index, position ->
            val startPosition = position - index
            result = result.replaceRange(startPosition, startPosition + 1, "")
        }

        return result
    }
}